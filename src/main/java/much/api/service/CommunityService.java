package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.CommunityCategory;
import much.api.common.util.ContextUtils;
import much.api.common.util.EditorUtils;
import much.api.dto.request.CommunityPostCreation;
import much.api.dto.response.CommunityPostDetail;
import much.api.entity.Community;
import much.api.entity.User;
import much.api.repository.CommunityRepository;
import much.api.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static much.api.common.enums.MuchType.COMMUNITY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityService {

    private final CommonService commonService;

    private final CommunityRepository communityRepository;

    private final FileRepository fileRepository;

    private final TagHelperService tagHelperService;

    @Transactional
    public CommunityPostDetail createPost(CommunityPostCreation postCreation) {

        // 사용자 확인
        Long userId = ContextUtils.getUserId();
        User user = commonService.getUserOrThrowException(userId);

        // 글 저장
        final CommunityCategory category = postCreation.getCategory();
        final String requestContent = postCreation.getContent();
        final List<String> requestTags = postCreation.getTags();

        Community post = Community.builder()
                .author(user)
                .category(category)
                .content(requestContent)
                .build();
        communityRepository.save(post);

        // 에디터로 업로드 된 파일 관리정보 업데이트
        List<String> imageFilenames = EditorUtils.extractImageFilenamesAtHtml(requestContent);
        fileRepository.setRelation(COMMUNITY, post.getId(), imageFilenames);

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

}
