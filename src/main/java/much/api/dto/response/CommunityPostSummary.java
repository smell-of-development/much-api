package much.api.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class CommunityPostSummary {

    private String category;

    private String title;

    private String content;

    private List<String> tags;

}
