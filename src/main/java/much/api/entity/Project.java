package much.api.entity;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import much.api.common.util.ContextUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "tb_project",
        indexes = {
                @Index(name = "tb_community_idx1", columnList = "deadline"),
        }
)
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private User writer;

    private String title;

    private String imageUrl;

    private boolean online;

    private String address;

    @Temporal(TemporalType.DATE)
    private LocalDate deadline;

    @Temporal(TemporalType.DATE)
    private LocalDate startDate;

    @Temporal(TemporalType.DATE)
    private LocalDate endDate;

    private String timesPerWeek;

    @Column(columnDefinition = "text")
    private String introduction;

    @Column(columnDefinition = "text")
    private String introductionWithoutHtmlTags;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectPosition> positionStatus = new ArrayList<>();

    private long viewCount;

    @Builder
    public Project(User writer,
                   String title,
                   String imageUrl,
                   boolean online,
                   String address,
                   LocalDate deadline,
                   LocalDate startDate,
                   LocalDate endDate,
                   String timesPerWeek,
                   String introduction,
                   String introductionWithoutHtmlTags) {

        this.writer = writer;
        this.title = title;
        this.imageUrl = imageUrl;
        this.online = online;
        this.address = StringUtils.isBlank(address) ? "협의" : address;
        this.deadline = deadline;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timesPerWeek = StringUtils.isBlank(timesPerWeek) ? "협의" : timesPerWeek;
        this.introduction = introduction;
        this.introductionWithoutHtmlTags = introductionWithoutHtmlTags;
    }


    public boolean isWriter() {

        Long contextUserId = ContextUtils.getUserId();
        return writer.getId().equals(contextUserId);
    }


    public void increaseViewCount() {

        ++viewCount;
    }


    public String getSchedule() {

        String schedule;
        if (startDate == null || endDate == null) {
            schedule = "협의";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

            String start = startDate.format(formatter);
            String end = endDate.format(formatter);
            long between = ChronoUnit.DAYS.between(startDate, endDate);

            schedule = String.format("%s ~ %s (%s일)", start, end, between);
        }

        return schedule;
    }


    public long getDeadlineDDay() {

        return ChronoUnit.DAYS.between(LocalDate.now(), deadline);
    }

}
