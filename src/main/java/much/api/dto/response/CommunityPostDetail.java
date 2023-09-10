package much.api.dto.response;

import lombok.Builder;
import lombok.Getter;
import much.api.common.enums.CommunityCategory;
import much.api.entity.Community;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
public class CommunityPostDetail {

    private final Long id;

    private final boolean editable;

    private final CommunityCategory category;

    private final Set<String> tags;

    private final String title;

    private final String content;

    private final Long authorId;

    private final String authorNickname;

    private final String authorImageUrl;

    private final LocalDateTime createdAt;

    private final Long viewCount;


    @Builder
    private CommunityPostDetail(Long id,
                                boolean editable,
                                CommunityCategory category,
                                Set<String> tags,
                                String title,
                                String content,
                                Long authorId,
                                String authorNickname,
                                String authorImageUrl,
                                LocalDateTime createdAt,
                                Long viewCount) {

        this.id = id;
        this.editable = editable;
        this.category = category;
        this.tags = tags;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
        this.authorImageUrl = authorImageUrl;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
    }


    public static CommunityPostDetail ofEntity(Community community, Set<String> tags) {

        return CommunityPostDetail.builder()
                .id(community.getId())
                .editable(community.isAuthor())
                .category(community.getCategory())
                .tags(tags)
                .title(community.getTitle())
                .content(community.getContent())
                .authorId(community.getAuthor().getId())
                .authorNickname(community.getAuthor().getNickname())
                .authorImageUrl(community.getAuthor().getImageUrl())
                .createdAt(community.getCreatedAt())
                .viewCount(community.getViewCount())
                .build();
    }

}
