package much.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import much.api.common.enums.CommunityCategory;
import much.api.entity.Community;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CommunityPostDetail {

    private Long id;

    private boolean editable;

    private CommunityCategory category;

    private Set<String> tags;

    private String title;

    private String content;

    private Long authorId;

    private String authorNickname;

    private String authorImageUrl;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    @Schema(example = "yyyy.MM.dd HH:mm", type = "string")
    private LocalDateTime createdAt;

    private Long viewCount;

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
