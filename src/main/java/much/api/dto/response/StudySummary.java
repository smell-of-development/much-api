package much.api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StudySummary {

    private Long id;

    private String title;

    private List<String> tags;

    private Boolean online;

    private String address;

    private Long timesPerWeek;

    private Long viewCount;

    private Long deadlineDDay;

    private Boolean pick;

    private String imageUrl;

    private StudyDetail.Recruit recruit;

    @Builder
    public StudySummary(Long id,
                        String title,
                        List<String> tags,
                        Boolean online,
                        String address,
                        Long timesPerWeek,
                        Long viewCount,
                        Long deadlineDDay,
                        Boolean pick,
                        String imageUrl,
                        Integer needs,
                        Integer recruited) {

        this.id = id;
        this.title = title;
        this.tags = tags;
        this.online = online;
        this.address = address;
        this.timesPerWeek = timesPerWeek;
        this.viewCount = viewCount;
        this.deadlineDDay = deadlineDDay;
        this.pick = pick;
        this.imageUrl = imageUrl;
        this.recruit = StudyDetail.Recruit.of(needs <= recruited, needs, recruited);
    }
}
