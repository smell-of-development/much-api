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
public class CommunityPostCreation {

    @NotNull
    private CommunityCategory category;

    private Set<String> tags = new HashSet<>();

    private String content;


    @Builder
    private CommunityPostCreation(CommunityCategory category,
                                  Set<String> tags,
                                  String content) {

        this.category = category;
        this.tags = tags;
        this.content = content;
    }
}
