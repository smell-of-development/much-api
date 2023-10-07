package much.api.dto.response;

import java.util.List;

public class ProjectSummary {

    private Long id;

    private String title;

    private List<String> tags;

    private boolean online;

    private String address;

    private String timesPerWeek;

    private long viewCount;

    private long deadlineDDay;

    private boolean pick;

    private String imageUrl;

    private ProjectDetail.Recruit recruit;

}
