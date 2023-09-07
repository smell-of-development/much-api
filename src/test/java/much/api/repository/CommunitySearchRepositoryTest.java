package much.api.repository;

import lombok.extern.slf4j.Slf4j;
import much.api.WithUser;
import much.api.common.enums.CommunityCategory;
import much.api.dto.request.CommunityPostCreation;
import much.api.dto.request.CommunitySearch;
import much.api.dto.response.CommunityPostDetail;
import much.api.service.CommunityService;
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

    @Test
    @DisplayName("커뮤니티 태그검색 간단 테스트")
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void test() {

        CommunityPostCreation p1 = CommunityPostCreation.builder()
                .category(CommunityCategory.QNA)
                .title("test1")
                .content("test1")
                .tags(Set.of("Spring Boot", "JPA", "React"))
                .build();

        CommunityPostCreation p2 = CommunityPostCreation.builder()
                .category(CommunityCategory.FREE)
                .title("test2")
                .content("test2")
                .tags(Set.of("Spring Boot", "JAVA"))
                .build();

        CommunityPostCreation p3 = CommunityPostCreation.builder()
                .category(CommunityCategory.QNA)
                .title("test3")
                .content("test3")
                .tags(Set.of("JAVA", "React"))
                .build();

        communityService.createPost(p1);
        communityService.createPost(p2);
        CommunityPostDetail postDetail3 = communityService.createPost(p3);

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
        assertEquals(postDetail3.getId(), content.get(0).getId());
        assertEquals(postDetail3.getTags(), Set.of(content.get(0).getTags().split(",")));
        assertEquals(postDetail3.getCategory(), content.get(0).getCategory());
        assertEquals(postDetail3.getTitle(), content.get(0).getTitle());
        assertEquals(postDetail3.getContent(), content.get(0).getContent());
        assertEquals("테스트", content.get(0).getAuthorNickname());
    }

}