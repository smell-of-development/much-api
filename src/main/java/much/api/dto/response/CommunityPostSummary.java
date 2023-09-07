package much.api.dto.response;

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

    // TODO
    private Long viewCount;

    // TODO
    private Long commentCount;

    private LocalDateTime createdAt;
}
