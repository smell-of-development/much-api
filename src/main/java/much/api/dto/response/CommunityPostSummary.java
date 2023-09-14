package much.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
    private LocalDateTime createdAt;
}
