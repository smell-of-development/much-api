package much.api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import much.api.common.exception.NoAuthority;
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
                @Index(name = "tb_project_application_idx2", columnList = "project_id, member_id"),
                @Index(name = "tb_project_application_idx3", columnList = "member_id")
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
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User member;

    private String memo;

    @Builder
    private ProjectApplication(Project project,
                               ProjectPosition position,
                               User member,
                               String memo) {

        this.project = project;
        this.position = position;
        this.member = member;
        this.memo = memo;
    }

    public void accept() {

        // 승인 권한 확인
        boolean isWriter = project.isWriter();
        if (!isWriter) {
            throw new NoAuthority("프로젝트 신청서 승인");
        }

        position.addPositionJoin(
                ProjectJoin.builder()
                        .project(project)
                        .position(position)
                        .member(member)
                        .build());

        position.getPositionApplications().remove(this);
    }
}
