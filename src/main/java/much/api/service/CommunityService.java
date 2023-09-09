package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.CommunityCategory;
import much.api.common.exception.NoAuthority;
import much.api.common.exception.PostNotFound;
import much.api.common.util.ContextUtils;
import much.api.dto.request.CommunityPostCreation;
import much.api.dto.request.CommunityPostModification;
import much.api.dto.request.CommunitySearch;
import much.api.dto.response.CommunityPostDetail;
import much.api.dto.response.CommunityPostSummary;
import much.api.dto.response.PagedResult;
import much.api.entity.Community;
import much.api.entity.User;
import much.api.repository.CommunityRepository;
import much.api.repository.CommunitySearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static much.api.common.enums.MuchType.COMMUNITY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityService {

    private final CommonService commonService;

    private final FileService fileService;

    private final TagHelperService tagHelperService;

    private final CommunitySearchRepository communitySearchRepository;

    private final CommunityRepository communityRepository;


    // TODO GET POSTS 테스트 작성, 단건조회 구현
    public PagedResult<CommunityPostSummary> getPosts(CommunitySearch searchCondition) {

        Page<CommunitySearchRepository.CommunitySearchDto> page = communitySearchRepository.searchCommunityPosts(searchCondition);
        // noinspection unchecked
        return (PagedResult<CommunityPostSummary>) PagedResult.ofPage(page);
    }


    @Transactional
    public CommunityPostDetail createPost(CommunityPostCreation postCreation) {

        // 사용자 확인
        Long userId = ContextUtils.getUserId();
        User user = commonService.getUserOrThrowException(userId);

        // 글 저장
        final CommunityCategory category = postCreation.getCategory();
        final String requestTitle = postCreation.getTitle();
        final String requestContent = postCreation.getContent();
        final Set<String> requestTags = postCreation.getTags();

        Community post = Community.builder()
                .author(user)
                .category(category)
                .title(requestTitle)
                .content(requestContent)
                .build();

        Community saved = communityRepository.save(post);

        // 에디터로 업로드 된 파일 관리정보 업데이트
        fileService.handleEditorImage(COMMUNITY, post.getId(), requestContent);

        // 태그정보, 관계정보 반영
        tagHelperService.handleTagRelation(COMMUNITY, post.getId(), requestTags);

        // 응답
        return CommunityPostDetail.builder()
                .id(saved.getId())
                .editable(true)
                .category(saved.getCategory())
                .tags(requestTags)
                .title(saved.getTitle())
                .content(saved.getContent())
                .authorId(user.getId())
                .authorNickname(user.getNickname())
                .authorImageUrl(user.getImageUrl())
                .build();
    }


    @Transactional
    public CommunityPostDetail modifyPost(Long postId,
                                          CommunityPostModification postModification) {

        // 사용자 확인
        Long userId = ContextUtils.getUserId();
        User user = commonService.getUserOrThrowException(userId);

        final CommunityCategory category = postModification.getCategory();

        // 기존글 조회
        Community post = communityRepository.findPostWithUser(postId)
                .orElseThrow(() -> new PostNotFound(category.name(), postId));

        if (!post.isAuthor(userId)) {
            throw new NoAuthority("게시글 수정");
        }

        final String modifiedTitle = postModification.getTitle();
        final String modifiedContent = postModification.getContent();
        final Set<String> tags = postModification.getTags();

        // 내용수정
        post.modify(modifiedTitle, modifiedContent);

        // 태그정보 수정
        tagHelperService.handleTagRelation(COMMUNITY, postId, tags);

        // 에디터 파일정보 수정
        fileService.handleEditorImage(COMMUNITY, postId, modifiedContent);

        return CommunityPostDetail.builder()
                .id(post.getId())
                .editable(true)
                .category(post.getCategory())
                .tags(tags)
                .title(post.getTitle())
                .content(post.getContent())
                .authorId(user.getId())
                .authorNickname(user.getNickname())
                .authorImageUrl(user.getImageUrl())
                .build();
    }


    @Transactional
    public void deletePost(Long postId) {

        // 사용자 확인
        Long userId = ContextUtils.getUserId();
        commonService.getUserOrThrowException(userId);

        // 기존글 조회
        Community post = communityRepository.findPostWithUser(postId)
                .orElseThrow(() -> new PostNotFound(postId));

        if (!post.isAuthor(userId)) {
            throw new NoAuthority("게시글 수정");
        }

        // 태그정보 삭제
        tagHelperService.deleteTagRelation(COMMUNITY, postId);

        // 파일정보 관리
        fileService.releaseEditorImage(COMMUNITY, postId);

        // 글 삭제
        communityRepository.deleteById(postId);
    }
}
