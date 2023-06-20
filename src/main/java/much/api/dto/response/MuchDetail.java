package much.api.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MuchDetail {

    private Long id;

    private String title;

    private WriterDetail writer;

    private String imageUrl;

    private boolean isOnline;

    private String location;

    private LocalDateTime deadline;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String schedule;

    private String target;

    private Integer currentTotal;

    private Integer maximumPeople;

    private String introduction;

    private List<SkillDetail> skills;

    private List<WorkDetail> work;

    @Getter
    @Builder
    public static class WriterDetail {

        private Long id;

        private String nickname;

        private String pictureUrl;

    }


    @Getter
    @Builder
    public static class SkillDetail {

        private String name;

        private String imageUrl;

    }

    @Getter
    @Builder
    public static class WorkDetail {

        private String position;

        private Integer current;

        private Integer needs;

    }

}
