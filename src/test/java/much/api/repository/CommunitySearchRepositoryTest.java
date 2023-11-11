package much.api.repository;

import lombok.extern.slf4j.Slf4j;
import much.api.WithUser;
import much.api.common.enums.CommunityCategory;
import much.api.dto.request.CommunityPostForm;
import much.api.dto.request.CommunitySearch;
import much.api.dto.response.CommunityPostDetail;
import much.api.service.CommunityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
class CommunitySearchRepositoryTest {

    @Autowired
    CommunitySearchRepository communitySearchRepository;

    @Autowired
    CommunityService communityService;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TagRelationRepository tagRelationRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
        tagRelationRepository.deleteAll();
        communityRepository.deleteAll();
    }

    @Test
    @DisplayName("커뮤니티 태그검색 간단 테스트")
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void test() {

        CommunityPostForm p1 = CommunityPostForm.builder()
                .category(CommunityCategory.QNA)
                .title("test1")
                .content("test1")
                .tags(Set.of("Spring Boot", "JPA", "React", "JAVA"))
                .build();

        CommunityPostForm p2 = CommunityPostForm.builder()
                .category(CommunityCategory.FREE)
                .title("test2")
                .content("test2")
                .tags(Set.of("Spring Boot", "JAVA"))
                .build();

        CommunityPostForm p3 = CommunityPostForm.builder()
                .category(CommunityCategory.QNA)
                .title("test3")
                .content("test3")
                .tags(Set.of("JAVA", "React"))
                .build();

        CommunityPostDetail postDetail1 = communityService.createPost(p1);
        communityService.createPost(p2);
        communityService.createPost(p3);

        Page<CommunitySearchRepository.CommunitySearchDto> communitySearchDtos = communitySearchRepository.searchCommunityPosts(
                CommunitySearch.builder()
                        .category("QNA")
                        .search("")
                        .tags(List.of("Spring Boot", "JAVA"))
                        .page(1)
                        .size(1)
                        .byRecent(false)
                        .build()
        );

        assertEquals(1, communitySearchDtos.getSize());
        assertEquals(1, communitySearchDtos.getNumberOfElements());
        assertEquals(2, communitySearchDtos.getTotalPages());

        List<CommunitySearchRepository.CommunitySearchDto> content = communitySearchDtos.getContent();
        assertEquals(postDetail1.getId(), content.get(0).getId());
        assertEquals(postDetail1.getTags(), Set.of(content.get(0).getTags().split(",")));
        assertEquals(postDetail1.getCategory(), content.get(0).getCategory());
        assertEquals(postDetail1.getTitle(), content.get(0).getTitle());
        assertEquals(postDetail1.getContent(), content.get(0).getContent());
        assertEquals("테스트", content.get(0).getAuthorNickname());
    }

}