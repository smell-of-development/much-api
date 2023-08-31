package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.CommunityCategory;
import much.api.common.exception.NoAuthority;
import much.api.common.exception.PostNotFound;
import much.api.common.util.ContextUtils;
import much.api.dto.request.CommunityPostCreation;
import much.api.dto.request.CommunityPostModification;
import much.api.dto.response.CommunityPostDetail;
import much.api.entity.Community;
import much.api.entity.User;
import much.api.repository.CommunityRepository;
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

    private final CommunityRepository communityRepository;

    private final TagHelperService tagHelperService;

    @Transactional
    public CommunityPostDetail createPost(CommunityPostCreation postCreation) {

        // 사용자 확인
        Long userId = ContextUtils.getUserId();
        User user = commonService.getUserOrThrowException(userId);

        // 글 저장
        final CommunityCategory category = postCreation.getCategory();
        final String requestContent = postCreation.getContent();
        final Set<String> requestTags = postCreation.getTags();

        Community post = Community.builder()
                .author(user)
                .category(category)
                .content(requestContent)
                .build();
        communityRepository.save(post);

        // 에디터로 업로드 된 파일 관리정보 업데이트
        fileService.handleEditorImage(COMMUNITY, post.getId(), requestContent);

        // 태그정보, 관계정보 반영
        tagHelperService.handleTagInformation(COMMUNITY, post.getId(), requestTags);

        // 응답
        return CommunityPostDetail.builder()
                .id(post.getId())
                .editable(true)
                .category(category)
                .tags(requestTags)
                .content(requestContent)
                .authorId(userId)
                .authorNickname(user.getNickname())
                .authorImageUrl(user.getImageUrl())
                .build();
    }


    @Transactional
    public CommunityPostDetail modifyPost(Long postId,
                                          CommunityPostModification postModification) {

        final CommunityCategory category = postModification.getCategory();

        // 기존글 조회
        Community post = communityRepository.findPostWithUser(postId, category)
                .orElseThrow(() -> new PostNotFound(category.name(), postId));

        Long userId = ContextUtils.getUserId();
        if (!post.isAuthor(userId)) {
            throw new NoAuthority("게시글 수정");
        }

        final String modifiedContent = postModification.getContent();
        final Set<String> tags = postModification.getTags();

        // 내용수정
        post.modify(modifiedContent);

        // 태그정보 수정
        tagHelperService.handleTagInformation(COMMUNITY, postId, tags);

        // 에디터 파일정보 수정
        fileService.handleEditorImage(COMMUNITY, postId, modifiedContent);

        return CommunityPostDetail.builder()
                .id(postId)
                .editable(true)
                .category(category)
                .tags(tags)
                .content(modifiedContent)
                .authorId(post.getAuthor().getId())
                .authorNickname(post.getAuthor().getNickname())
                .authorImageUrl(post.getAuthor().getImageUrl())
                .build();
    }
}
