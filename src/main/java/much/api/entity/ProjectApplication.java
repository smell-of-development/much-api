package much.api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "tb_project_application",
        indexes = {
                @Index(name = "tb_project_application_idx1", columnList = "project_id, position_id"),
                @Index(name = "tb_project_application_idx2", columnList = "member_id")
        }
)
public class ProjectApplication extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProjectPosition position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User member;

    boolean approved;

    @Builder
    private ProjectApplication(Project project,
                               User member,
                               ProjectPosition position) {

        this.project = project;
        this.member = member;
        this.position = position;
        this.approved = false;
    }
}
