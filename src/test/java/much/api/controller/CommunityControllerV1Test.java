package much.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import much.api.WithUser;
import much.api.common.enums.CommunityCategory;
import much.api.common.util.EditorUtils;
import much.api.dto.request.CommunityPostCreation;
import much.api.entity.File;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommunityControllerV1Test {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

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
                    .category(CommunityCategory.valueOf(accessor.getString(0)))
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
    void community_create_test1(@AggregateWith(PostCreationAggregator.class) CommunityPostCreation information) throws Exception {
        // given
        String request = objectMapper.writeValueAsString(information);

        // 에디터 내 이미지파일 업로드됨 가정
        List<String> imageFilenames = EditorUtils.extractImageFilenamesAtHtml(information.getContent());
        imageFilenames.forEach(name ->
                fileRepository.save(
                        File.builder()
                                .storedFilename(name)
                                .build())
        );

        // expected
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        mockMvc.perform(
                        post("/api/v1/community")
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.requires").isEmpty())
                .andExpect(jsonPath("$.result.id").isNumber())
                .andExpect(jsonPath("$.result.editable").value(true))
                .andExpect(jsonPath("$.result.category").value(information.getCategory().name()))
                .andExpect(jsonPath("$.result.tags.length()").value(information.getTags().size()))
                .andExpect(jsonPath("$.result.content").value(information.getContent()))
                .andExpect(jsonPath("$.result.authorId").value(userId))
                .andExpect(jsonPath("$.result.authorNickname").isNotEmpty())
                .andExpect(jsonPath("$.result.authorImageUrl").hasJsonPath());
    }

}