package much.api.service;

import much.api.WithUser;
import much.api.common.util.EditorUtils;
import much.api.dto.request.CommunityPostCreation;
import much.api.dto.response.CommunityPostDetail;
import much.api.entity.File;
import much.api.entity.Tag;
import much.api.entity.TagRelation;
import much.api.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.lang.String.join;
import static much.api.common.enums.CommunityCategory.valueOf;
import static much.api.common.enums.MuchType.COMMUNITY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CommunityServiceTest {

    @Autowired
    CommunityService communityService;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    TagRelationRepository tagRelationRepository;

    @BeforeEach
    void clean() {
        communityRepository.deleteAll();
        fileRepository.deleteAll();
        userRepository.deleteAll();
        tagRelationRepository.deleteAll();
        tagRepository.deleteAll();
    }

    static class PostCreationAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {

            return CommunityPostCreation.builder()
                    .category(accessor.getString(0))
                    .tags(List.of(accessor.getString(1).split("\\|")))
                    .content(accessor.getString(2))
                    .build();
        }
    }

    @ParameterizedTest(name = "[{index}] 카테고리: {0}, 태그: {1}, 내용: {2}")
    @DisplayName("커뮤니티 글 등록 성공")
    @CsvSource({
            "QNA, Spring Boot|Spring|Java, <tag><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "FREE, JPA|Java|Spring|React, <src><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "TECH_SHARE, Vue|Node|Python, <img><src><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
    })
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void community_create_test1(@AggregateWith(PostCreationAggregator.class) CommunityPostCreation postCreation) {
        // given

        // 에디터 내 이미지파일 업로드됨 가정
        List<String> imageFilenames = EditorUtils.extractImageFilenamesAtHtml(postCreation.getContent());
        imageFilenames.forEach(name ->
                fileRepository.save(
                        File.builder()
                                .storedFilename(name)
                                .build())
        );

        // when
        CommunityPostDetail postDetail = communityService.createPost(postCreation);

        // then
        // 이미지 파일 관계정보 정상반영 확인
        List<File> files = fileRepository.findAll();
        assertEquals(imageFilenames.size(), files.size());
        files.forEach(file -> {
            assertEquals(COMMUNITY, file.getRelationType());
            assertEquals(postDetail.getId(), file.getRelationId());
            assertTrue(imageFilenames.contains(file.getStoredFilename()));
        });

        // 태그정보 정상반영 확인
        List<Tag> tags = tagRepository.findAll();
        assertEquals(postCreation.getTags().size(), tags.size());

        List<TagRelation> tagRelations = tagRelationRepository.findAll();
        assertEquals(postCreation.getTags().size(), tagRelations.size());
        tagRelations.forEach(tl -> {
            assertEquals(COMMUNITY, tl.getRelationType());
            assertEquals(postDetail.getId(), tl.getRelationId());
        });

        // 리턴객체 정상 확인
        assertEquals(1L, communityRepository.findAll().size());
        assertEquals(valueOf(postCreation.getCategory()), postDetail.getCategory());
        assertEquals(join("|", postCreation.getTags()), join("|", postDetail.getTags()));
        assertEquals(postCreation.getContent(), postDetail.getContent());
    }

}