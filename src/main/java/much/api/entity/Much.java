package much.api.entity;

import jakarta.persistence.*;
import lombok.*;
import much.api.common.enums.MuchType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    private String skills;

    @Column(columnDefinition = "text")
    private String introduction;

    // 에디터 이미지 관리용
    private String ImageFileIds;

}
