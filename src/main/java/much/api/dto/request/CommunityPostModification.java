package much.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommunityPostModification {

    @NotNull
    private String category;

    @NotNull
    private Long id;

    private List<String> tags = new ArrayList<>();

    private String content;


    @Builder
    private CommunityPostModification(String category,
                                      Long id,
                                      List<String> tags,
                                      String content) {

        this.category = category;
        this.id = id;
        this.tags = tags;
        this.content = content;
    }
}
