package much.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import much.api.common.enums.CommunityCategory;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommunityPostModification {

    @NotNull
    private CommunityCategory category;

    private Set<String> tags = new HashSet<>();

    private String title;

    private String content;


    @Builder
    private CommunityPostModification(CommunityCategory category,
                                      Set<String> tags,
                                      String title,
                                      String content) {

        this.category = category;
        this.tags = tags;
        this.title = title;
        this.content = content;
    }
}
