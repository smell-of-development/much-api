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
public class CommunityPostCreation {

    @NotNull
    private String category;

    private List<String> tags = new ArrayList<>();

    private String content;


    @Builder
    private CommunityPostCreation(String category,
                                  List<String> tags,
                                  String content) {

        this.category = category;
        this.tags = tags;
        this.content = content;
    }
}
