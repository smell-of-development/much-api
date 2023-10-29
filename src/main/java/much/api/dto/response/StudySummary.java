package much.api.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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

    @Setter
    private StudyDetail.Recruit recruit;

    public StudySummary(Long id,
                        String title,
                        List<String> tags,
                        Boolean online,
                        String address,
                        Long timesPerWeek,
                        Long viewCount,
                        Long deadlineDDay,
                        Boolean pick,
                        String imageUrl) {

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
    }
}
