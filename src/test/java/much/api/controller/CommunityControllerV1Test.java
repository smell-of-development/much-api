package much.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import much.api.WithUser;
import much.api.common.enums.CommunityCategory;
import much.api.common.util.EditorUtils;
import much.api.dto.request.CommunityPostCreation;
import much.api.dto.request.CommunityPostModification;
import much.api.dto.response.CommunityPostDetail;
import much.api.entity.Community;
import much.api.entity.File;
import much.api.entity.User;
import much.api.repository.*;
import much.api.service.CommunityService;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static much.api.common.enums.CommunityCategory.FREE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    CommunityService communityService;

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
                            .category(CommunityCategory.valueOf(accessor.getString(1)))
                            .tags(Set.of(accessor.getString(2).split("\\|")))
                            .content(accessor.getString(3))
                            .build()
                    :
                    CommunityPostModification.builder()
                            .category(CommunityCategory.valueOf(accessor.getString(1)))
                            .tags(Set.of(accessor.getString(2).split("\\|")))
                            .content(accessor.getString(3))
                            .build();
        }
    }

    @ParameterizedTest(name = "[{index}] 카테고리: {1}, 태그: {2}, 내용: {3}")
    @DisplayName("커뮤니티 글 등록 실패 - 비로그인")
    @CsvSource({
            "POST, QNA, Spring Boot|Spring|Java, <tag><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "POST, FREE, JPA|Java|Spring|React, <src><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "POST, TECH_SHARE, Vue|Node|Python, <img><src><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
    })
    void community_create_test2(@AggregateWith(PostAggregator.class) CommunityPostCreation information) throws Exception {
        // given
        String request = objectMapper.writeValueAsString(information);

        mockMvc.perform(
                        post("/api/v1/communities")
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(4001))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.requires").isEmpty())
                .andExpect(jsonPath("$.result").isEmpty());
    }


    @ParameterizedTest(name = "[{index}] 카테고리: {1}, 태그: {2}, 내용: {3}")
    @DisplayName("커뮤니티 글 등록 성공")
    @CsvSource({
            "POST, QNA, Spring Boot|Spring|Java, <tag><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "POST, FREE, JPA|Java|Spring|React, <src><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "POST, TECH_SHARE, Vue|Node|Python, <img><src><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
    })
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void community_create_test1(@AggregateWith(PostAggregator.class) CommunityPostCreation information) throws Exception {
        // given
        String request = objectMapper.writeValueAsString(information);

        // 에디터 내 이미지파일 업로드됨 가정
        List<String> imageFilenames = EditorUtils.extractImageFilenamesAtHtml(information.getContent());
        imageFilenames.forEach(name ->
                fileRepository.save(
                        File.builder()
                                .storedFilename(name)
                                .released(true)
                                .build())
        );

        // expected
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        mockMvc.perform(
                        post("/api/v1/communities")
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


    @ParameterizedTest(name = "[{index}] 카테고리: {1}, 태그: {2}, 내용: {3}")
    @DisplayName("커뮤니티 글 수정 성공")
    @CsvSource({
            "PUT, QNA, Spring Boot|Spring|Java, <tag><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "PUT, FREE, JPA|Java|Spring|React, <src><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "PUT, TECH_SHARE, Vue|Node|Python, <img><src><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
    })
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void community_modify_test1(@AggregateWith(PostAggregator.class) CommunityPostModification information) throws Exception {
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

        String request = objectMapper.writeValueAsString(information);

        // expected
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        mockMvc.perform(
                        put("/api/v1/communities/{postId}", saved.getId())
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


    @ParameterizedTest(name = "[{index}] 카테고리: {1}, 태그: {2}, 내용: {3}")
    @DisplayName("커뮤니티 글 수정 실패 - 비로그인")
    @CsvSource({
            "PUT, QNA, Spring Boot|Spring|Java, <tag><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "PUT, FREE, JPA|Java|Spring|React, <src><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "PUT, TECH_SHARE, Vue|Node|Python, <img><src><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
    })
    void community_modify_test2(@AggregateWith(PostAggregator.class) CommunityPostModification information) throws Exception {
        // given
        String request = objectMapper.writeValueAsString(information);

        // expected
        mockMvc.perform(
                        put("/api/v1/communities/{postId}", 1L)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(4001))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.requires").isEmpty())
                .andExpect(jsonPath("$.result").isEmpty());
    }


    @ParameterizedTest(name = "[{index}] 카테고리: {1}, 태그: {2}, 내용: {3}")
    @DisplayName("커뮤니티 글 수정 실패 - 작성자가 아님")
    @CsvSource({
            "PUT, QNA, Spring Boot|Spring|Java, <tag><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "PUT, FREE, JPA|Java|Spring|React, <src><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "PUT, TECH_SHARE, Vue|Node|Python, <img><src><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
    })
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void community_modify_test3(@AggregateWith(PostAggregator.class) CommunityPostModification information) throws Exception {
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

        String request = objectMapper.writeValueAsString(information);

        // expected
        mockMvc.perform(
                        put("/api/v1/communities/{postId}", saved.getId())
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(4003))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.requires").isEmpty())
                .andExpect(jsonPath("$.result").isEmpty());
    }


    @ParameterizedTest(name = "[{index}] 카테고리: {1}, 태그: {2}, 내용: {3}")
    @DisplayName("커뮤니티 글 수정 실패 - 없는 게시글")
    @CsvSource({
            "PUT, QNA, Spring Boot|Spring|Java, <tag><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "PUT, FREE, JPA|Java|Spring|React, <src><img><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
            "PUT, TECH_SHARE, Vue|Node|Python, <img><src><img src='image/storedImageFile1'><><tag><img><img src='image/storedImageFile2'>",
    })
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void community_modify_test4(@AggregateWith(PostAggregator.class) CommunityPostModification information) throws Exception {
        // given
        String request = objectMapper.writeValueAsString(information);

        // expected
        mockMvc.perform(
                        put("/api/v1/communities/{postId}", 0L)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(2000))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.requires").isEmpty())
                .andExpect(jsonPath("$.result").isEmpty());
    }


    @Test
    @DisplayName("커뮤니티 글 삭제 성공")
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void community_delete_test1() throws Exception {
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

        // expected
        mockMvc.perform(
                        delete("/api/v1/communities/{postId}", saved.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.requires").isEmpty())
                .andExpect(jsonPath("$.result").isEmpty());
    }
}