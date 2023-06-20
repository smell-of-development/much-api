package much.api.entity;

import jakarta.persistence.*;
import lombok.*;
import much.api.common.enums.MuchType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_MUCH")
public class Much extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private User writer;

    @Enumerated(EnumType.STRING)
    private MuchType type;

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

    @OneToMany(mappedBy = "much", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkPosition> workPositions = new ArrayList<>();

    private String skills;

    @Column(columnDefinition = "text")
    private String introduction;

    @Builder
    public Much(User writer, MuchType type, String title, String imageUrl, boolean isOnline, String location,
                LocalDateTime deadline, LocalDateTime startDate, LocalDateTime endDate, String schedule,
                String target, Integer maximumPeople, String skills, String introduction) {

        this.writer = writer;
        this.type = type;
        this.title = title;
        this.imageUrl = imageUrl;
        this.isOnline = isOnline;
        this.location = location;
        this.deadline = deadline;
        this.startDate = startDate;
        this.endDate = endDate;
        this.schedule = schedule;
        this.target = target;
        this.maximumPeople = maximumPeople;
        this.skills = skills;
        this.introduction = introduction;
    }

    public void addWorkPosition(WorkPosition workPosition) {

        workPositions.add(workPosition);
        workPosition.setMuch(this);
    }

}
