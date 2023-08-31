package much.api.dto.response;

import lombok.Builder;
import lombok.Getter;
import much.api.common.enums.CommunityCategory;

import java.util.Set;

@Getter
public class CommunityPostDetail {

    private final Long id;

    private final boolean editable;

    private final CommunityCategory category;

    private final Set<String> tags;

    private final String content;

    private final Long authorId;

    private final String authorNickname;

    private final String authorImageUrl;


    @Builder
    private CommunityPostDetail(Long id,
                                boolean editable,
                                CommunityCategory category,
                                Set<String> tags,
                                String content,
                                Long authorId,
                                String authorNickname,
                                String authorImageUrl) {

        this.id = id;
        this.editable = editable;
        this.category = category;
        this.tags = tags;
        this.content = content;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
        this.authorImageUrl = authorImageUrl;
    }

}
