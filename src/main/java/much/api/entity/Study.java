package much.api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import much.api.common.exception.NoAuthority;
import much.api.common.util.ContextUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "tb_study",
        indexes = {
                @Index(name = "tb_study_idx1", columnList = "id, deadline"),
        }
)
public class Study extends BaseTimeEntity {

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
    private Integer needs;

    @Getter
    private Integer recruited;

    @Getter
    @Column(columnDefinition = "text")
    private String introduction;

    @Getter
    @Column(columnDefinition = "text")
    private String introductionWithoutHtmlTags;

    @Getter
    private long viewCount;

    @Getter
    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyJoin> studyJoins = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyApplication> applications = new ArrayList<>();


    @Builder
    public Study(User writer,
                 String title,
                 String imageUrl,
                 boolean online,
                 String address,
                 LocalDate deadline,
                 LocalDate startDate,
                 LocalDate endDate,
                 String meetingDays,
                 Integer needs,
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
        this.needs = needs;
        this.introduction = introduction;
        this.introductionWithoutHtmlTags = introductionWithoutHtmlTags;
    }


    public boolean isWriter() {

        Long contextUserId = ContextUtils.getUserId();
        return writer.getId().equals(contextUserId);
    }


    public boolean hasJoinedUser(Long userId) {
        if (userId == null) return false;

        return studyJoins.stream()
                .anyMatch(sj -> sj.getMember().getId().equals(userId));
    }


    public boolean hasAppliedUser(Long userId) {
        if (userId == null) return false;

        return applications.stream()
                .anyMatch(ap -> ap.getMember().getId().equals(userId));
    }


    public void increaseViewCount() {

        ++viewCount;
    }


    public boolean closed() {

        return needs <= recruited;
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
                       Integer needs,
                       String introduction) {

        if (!isWriter()) {
            throw new NoAuthority("스터디 수정");
        }

        this.title = title;
        this.imageUrl = imageUrl;
        this.online = online;
        this.address = address;
        this.deadline = deadline;
        this.startDate = startDate;
        this.endDate = endDate;
        this.meetingDays = timesPerWeek;
        this.needs = needs;
        this.introduction = introduction;
    }


    public void addMember(User user) {

        if (!isWriter()) {
            throw new NoAuthority("스터디원 추가");
        }

        studyJoins.add(
                StudyJoin.builder()
                        .study(this)
                        .member(user)
                        .build()
        );
        recruited = studyJoins.size();
    }

}
