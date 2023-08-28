package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.CommunityCategory;
import much.api.common.util.ContextUtils;
import much.api.common.util.EditorUtils;
import much.api.dto.request.CommunityPostCreation;
import much.api.dto.response.CommunityPostDetail;
import much.api.entity.Community;
import much.api.entity.User;
import much.api.exception.MuchException;
import much.api.exception.UserNotFound;
import much.api.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static much.api.common.enums.Code.DEV_MESSAGE;
import static much.api.common.enums.MuchType.COMMUNITY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityService {

    private final UserRepository userRepository;

    private final CommunityRepository communityRepository;

    private final FileRepository fileRepository;

    private final TagHelperService tagHelperService;

    @Transactional
    public CommunityPostDetail createPost(CommunityPostCreation postCreation) {

        // 사용자 확인
        Long userId = ContextUtils.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound(userId));

        // 카테고리 확인
        CommunityCategory category;
        try {
            category = CommunityCategory.valueOf(postCreation.getCategory().toUpperCase());
        } catch (Exception e) {
            throw new MuchException(DEV_MESSAGE, "category 값이 잘못되었습니다.");
        }

        // 글 저장
        final String requestContent = postCreation.getContent();
        final List<String> requestTags = postCreation.getTags();

        String tags = String.join(",", requestTags);

        Community post = Community.builder()
                .author(user)
                .category(category)
                .content(requestContent)
                .tags(tags)
                .build();
        communityRepository.save(post);

        // 에디터로 업로드 된 파일에 연관정보 업데이트
        List<String> imageFilenames = EditorUtils.extractImageFilenamesAtHtml(requestContent);
        fileRepository.updateRelationInformation(COMMUNITY, post.getId(), imageFilenames);

        // 태그정보 반영
        tagHelperService.saveTagInformation(COMMUNITY, post.getId(), requestTags);

        // 응답
        return CommunityPostDetail.builder()
                .id(post.getId())
                .category(category)
                .tags(requestTags)
                .content(requestContent)
                .authorId(userId)
                .authorNickname(user.getNickname())
                .authorImageUrl(user.getImageUrl())
                .build();
    }

}
