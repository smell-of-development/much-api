package much.api.dto.response;

import lombok.Builder;
import lombok.Getter;
import much.api.common.enums.CommunityCategory;

import java.util.List;

@Getter
public class CommunityPostDetail {

    private final Long id;

    private final CommunityCategory category;

    private final List<String> tags;

    private final String content;

    private final Long authorId;

    private final String authorNickname;

    private final String authorImageUrl;


    @Builder
    private CommunityPostDetail(Long id,
                                CommunityCategory category,
                                List<String> tags,
                                String content,
                                Long authorId,
                                String authorNickname,
                                String authorImageUrl) {

        this.id = id;
        this.category = category;
        this.tags = tags;
        this.content = content;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
        this.authorImageUrl = authorImageUrl;
    }

}
