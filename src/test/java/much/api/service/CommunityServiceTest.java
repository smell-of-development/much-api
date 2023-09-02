package much.api.service;

import lombok.extern.slf4j.Slf4j;
import much.api.WithUser;
import much.api.common.exception.NoAuthority;
import much.api.common.exception.PostNotFound;
import much.api.dto.request.CommunityPostCreation;
import much.api.dto.request.CommunityPostModification;
import much.api.dto.response.CommunityPostDetail;
import much.api.entity.*;
import much.api.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Set;

import static much.api.common.enums.CommunityCategory.FREE;
import static much.api.common.enums.CommunityCategory.valueOf;
import static much.api.common.enums.MuchType.COMMUNITY;
import static much.api.common.util.EditorUtils.extractImageFilenamesAtHtml;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
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

    static class PostAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {

            return "POST".equals(accessor.getString(0)) ?
                    CommunityPostCreation.builder()
                            .category(valueOf(accessor.getString(1)))
                            .tags(Set.of(accessor.getString(2).split("\\|")))
                            .content(accessor.getString(3))
                            .build()
                    :
                    CommunityPostModification.builder()
                            .category(valueOf(accessor.getString(1)))
                            .tags(Set.of(accessor.getString(2).split("\\|")))
                            .content(accessor.getString(3))
                            .build();
        }
    }


    @ParameterizedTest(name = "[{index}] 카테고리: {1}, 태그: {2}, 내용: {3}")
    @DisplayName("커뮤니티 글 등록 성공")
    @CsvSource({
            "POST, QNA, Spring Boot|Spring|Java, <tag><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "POST, FREE, JPA|Java|Spring|React, <src><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "POST, TECH_SHARE, Vue|Node|Python, <img><src><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
    })
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void community_create_test1(@AggregateWith(PostAggregator.class) CommunityPostCreation postCreation) {
        // given

        // 에디터 내 이미지파일 업로드됨 가정
        List<String> imageFilenames = extractImageFilenamesAtHtml(postCreation.getContent());
        imageFilenames.forEach(name ->
                fileRepository.save(
                        File.builder()
                                .storedFilename(name)
                                .released(true)
                                .build())
        );

        // when
        log.info("시작 =========================================================");
        CommunityPostDetail postDetail = communityService.createPost(postCreation);
        log.info("종료 =========================================================");

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
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        assertEquals(1L, communityRepository.findAll().size());
        assertEquals(userId, postDetail.getAuthorId());
        assertTrue(postDetail.isEditable());
        assertEquals(postCreation.getCategory(), postDetail.getCategory());
        assertEquals(postCreation.getCategory(), postDetail.getCategory());
        assertEquals(postCreation.getContent(), postDetail.getContent());
    }


    @ParameterizedTest(name = "[{index}] 카테고리: {1}, 태그: {2}, 내용: {3}")
    @DisplayName("커뮤니티 글 수정 성공")
    @CsvSource({
            "PUT, QNA, Spring Boot|Spring|Java, <tag><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "PUT, FREE, JPA|Java|Spring|React, <src><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "PUT, TECH_SHARE, Vue|Node|Python, <img><src><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
    })
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void community_modify_test1(@AggregateWith(PostAggregator.class) CommunityPostModification information) {
        // given

        // 기존 게시글 준비
        CommunityPostCreation creation = CommunityPostCreation.builder()
                .category(information.getCategory())
                .tags(Set.of("JPA", "Adobe XD", "Node", "Java", "C++"))
                .content("<img src='image/storedImageFile0'> <img src='image/storedImageFile1'>")
                .build();

        // 기존 게시글의 이미지파일 관리정보
        File storedImageFile0 = File.builder()
                .storedFilename("storedImageFile0")
                .released(true)
                .build();
        File storedImageFile1 = File.builder()
                .storedFilename("storedImageFile1")
                .released(true)
                .build();
        fileRepository.saveAll(List.of(storedImageFile0, storedImageFile1));

        // 수정될 에디터 내 새로운 이미지파일 업로드됨 가정
        fileRepository.save(
                File.builder()
                        .storedFilename("storedImageFile2")
                        .released(true)
                        .build());

        CommunityPostDetail saved = communityService.createPost(creation);

        // when
        log.info("시작 =========================================================");
        CommunityPostDetail postDetail = communityService.modifyPost(saved.getId(), information);
        log.info("종료 =========================================================");

        // then
        // 이미지 파일 관계정보 정상반영 확인
        List<File> files = fileRepository.findAll();
        List<String> imageFilenames = extractImageFilenamesAtHtml(postDetail.getContent());

        assertEquals(imageFilenames.size() + 1, files.size()); // 삭제된 기존파일 1개 포함

        files.forEach(file -> {
            // 삭제된 이미지 관리정보 정상변경 확인
            if (file.isReleased()) {
                assertEquals(file.getStoredFilename(), storedImageFile0.getStoredFilename());
            } else {
                // 새로 업로드된 이미지파일 관리정보 정상변경 확인
                assertEquals(COMMUNITY, file.getRelationType());
                assertEquals(postDetail.getId(), file.getRelationId());
                assertTrue(imageFilenames.contains(file.getStoredFilename()));
            }
        });

        // 태그정보 정상반영 확인
        List<TagRelation> tagRelations = tagRelationRepository.findAll();
        assertEquals(information.getTags().size(), tagRelations.size());
        tagRelations.forEach(tl -> {
            assertEquals(COMMUNITY, tl.getRelationType());
            assertEquals(postDetail.getId(), tl.getRelationId());
        });

        // 리턴객체 정상 확인
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        assertEquals(1L, communityRepository.findAll().size());
        assertEquals(userId, postDetail.getAuthorId());
        assertTrue(postDetail.isEditable());
        assertEquals(information.getCategory(), postDetail.getCategory());
        assertEquals(information.getTags().size(), postDetail.getTags().size());
        assertEquals(information.getContent(), postDetail.getContent());
    }


    @ParameterizedTest(name = "[{index}] 카테고리: {1}, 태그: {2}, 내용: {3}")
    @DisplayName("커뮤니티 글 수정 실패 - 작성자가 아님")
    @CsvSource({
            "PUT, QNA, Spring Boot|Spring|Java, <tag><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "PUT, FREE, JPA|Java|Spring|React, <src><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "PUT, TECH_SHARE, Vue|Node|Python, <img><src><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
    })
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void community_modify_test2(@AggregateWith(PostAggregator.class) CommunityPostModification information) {
        // given

        // 다른 사용자, 기존 게시글
        User otherUser = User.builder()
                .loginId("otherUser")
                .build();
        userRepository.save(otherUser);

        Community saved = communityRepository.save(
                Community.builder()
                        .category(information.getCategory())
                        .author(otherUser)
                        .build());

        // expected
        assertThrows(NoAuthority.class, () -> communityService.modifyPost(saved.getId(), information));
    }


    @ParameterizedTest(name = "[{index}] 카테고리: {1}, 태그: {2}, 내용: {3}")
    @DisplayName("커뮤니티 글 수정 실패 - 없는 게시글")
    @CsvSource({
            "PUT, QNA, Spring Boot|Spring|Java, <tag><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "PUT, FREE, JPA|Java|Spring|React, <src><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "PUT, TECH_SHARE, Vue|Node|Python, <img><src><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
    })
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void community_modify_test3(@AggregateWith(PostAggregator.class) CommunityPostModification information) {

        // expected
        assertThrows(PostNotFound.class, () -> communityService.modifyPost(0L, information));
    }


    @Test
    @DisplayName("커뮤니티 글 삭제 성공")
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void community_delete_test1() {
        // given

        // 기존 게시글 준비
        CommunityPostCreation creation = CommunityPostCreation.builder()
                .category(FREE)
                .tags(Set.of("JPA", "Adobe XD", "Node", "Java", "C++"))
                .content("<img src='image/storedImageFile0'> <img src='image/storedImageFile1'>")
                .build();

        // 기존 게시글의 이미지파일 관리정보
        File storedImageFile0 = File.builder()
                .storedFilename("storedImageFile0")
                .released(true)
                .build();
        File storedImageFile1 = File.builder()
                .storedFilename("storedImageFile1")
                .released(true)
                .build();
        fileRepository.saveAll(List.of(storedImageFile0, storedImageFile1));

        CommunityPostDetail saved = communityService.createPost(creation);

        // when
        log.info("시작 =========================================================");
        communityService.deletePost(saved.getId());
        log.info("종료 =========================================================");

        // then
        assertTrue(communityRepository.findAll().isEmpty());
        assertTrue(tagRelationRepository.findAll().isEmpty());
        fileRepository.findAll()
                .forEach(file -> assertTrue(file.isReleased()));
    }
}