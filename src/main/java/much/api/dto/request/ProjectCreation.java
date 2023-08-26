package much.api.dto.request;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ProjectCreation {

    private String title;

    private String imageUrl;

    private boolean isOnline;

    private String location;

    private LocalDateTime deadline;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String schedule;

    private String target;

    private Integer maximumPeople;

    private String introduction;

    private List<String> skills;

    private List<Work> work;


    @Getter
    public static class Work {

        private String position;

        private Integer needs;

    }

}
