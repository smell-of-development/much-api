package much.api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import much.api.common.exception.NoAuthority;
import much.api.common.exception.PositionCanNotBeDeleted;
import much.api.common.util.ContextUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.function.Predicate.not;

@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "tb_project",
        indexes = {
                @Index(name = "tb_project_idx1", columnList = "id, deadline"),
        }
)
public class Project extends BaseTimeEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User writer;

    @Getter
    private String title;

    @Getter
    private String imageUrl;

    @Getter
    private boolean online;

    @Getter
    private String address;

    @Getter
    @Temporal(TemporalType.DATE)
    private LocalDate deadline;

    @Getter
    @Temporal(TemporalType.DATE)
    private LocalDate startDate;

    @Getter
    @Temporal(TemporalType.DATE)
    private LocalDate endDate;

    @Getter
    private String meetingDays;

    @Getter
    @Column(columnDefinition = "text")
    private String introduction;

    @Getter
    @Column(columnDefinition = "text")
    private String introductionWithoutHtmlTags;

    @Getter
    private long viewCount;

    @Getter
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectPosition> positionStatus = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "project")
    private List<ProjectJoin> projectMembers = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "project")
    private List<ProjectApplication> applications = new ArrayList<>();

    @Builder
    public Project(User writer,
                   String title,
                   String imageUrl,
                   boolean online,
                   String address,
                   LocalDate deadline,
                   LocalDate startDate,
                   LocalDate endDate,
                   String meetingDays,
                   String introduction,
                   String introductionWithoutHtmlTags) {

        this.writer = writer;
        this.title = title;
        this.imageUrl = imageUrl;
        this.online = online;
        this.address = address;
        this.deadline = deadline;
        this.startDate = startDate;
        this.endDate = endDate;
        this.meetingDays = meetingDays;
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


    public Long getBetween() {

        if (startDate == null || endDate == null) {
            return null;
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }


    public long getDeadlineDDay() {

        return ChronoUnit.DAYS.between(LocalDate.now(), deadline);
    }


    public void modify(String title,
                       String imageUrl,
                       Boolean online,
                       String address,
                       LocalDate deadline,
                       LocalDate startDate,
                       LocalDate endDate,
                       String timesPerWeek,
                       String introduction) {

        if (!isWriter()) {
            throw new NoAuthority("프로젝트 수정");
        }

        this.title = title;
        this.imageUrl = imageUrl;
        this.online = online;
        this.address = address;
        this.deadline = deadline;
        this.startDate = startDate;
        this.endDate = endDate;
        this.meetingDays = timesPerWeek;
        this.introduction = introduction;
    }


    public void addPosition(ProjectPosition position, boolean containsWriter) {

        positionStatus.add(position);

        if (containsWriter) {
            position.addPositionJoin(
                    ProjectJoin.builder()
                            .project(this)
                            .position(position)
                            .member(writer)
                            .build());
        }
    }


    public void deletePosition(Collection<ProjectPosition> positions) {

        positions.stream()
                .filter(not(ProjectPosition::isDeletable))
                .findAny()
                .ifPresent(pp -> {
                    throw new PositionCanNotBeDeleted(pp.getName());
                });

        positionStatus.removeAll(positions);
    }

}
