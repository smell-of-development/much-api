package much.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CommunityPostSummary {

    private Long id;

    private String category;

    private String title;

    private String content;

    private List<String> tags;

    private Long authorId;

    private String authorNickname;

    private String authorImageUrl;

    private Long viewCount;

    // TODO
    private Long commentCount;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    @Schema(example = "yyyy.MM.dd HH:mm", type = "string")
    private LocalDateTime createdAt;

}
