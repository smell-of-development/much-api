package much.api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import much.api.common.enums.MuchType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "tb_user_pick",
        indexes = {
                @Index(name = "tb_user_pick_idx1", columnList = "user_id, targetType, targetId")
        }
)
public class UserPick extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Enumerated(EnumType.STRING)
    private MuchType targetType;

    private Long targetId;

    private boolean available;


    @Builder
    private UserPick(User user, MuchType targetType, Long targetId) {

        this.user = user;
        this.targetType = targetType;
        this.targetId = targetId;
        this.available = false;
    }


    public boolean switchAvailable() {

        return available = !available;
    }
}


