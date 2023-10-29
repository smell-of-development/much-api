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
        name = "tb_study_application",
        indexes = {
                @Index(name = "tb_study_application_idx1", columnList = "study_id, member_id"),
                @Index(name = "tb_study_application_idx2", columnList = "member_id")
        }
)
public class StudyApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User member;

    private String memo;


    @Builder
    public StudyApplication(Study study, User member, String memo) {
        this.study = study;
        this.member = member;
        this.memo = memo;
    }

    public void accept() {

        // 승인 권한 확인
        boolean isWriter = study.isWriter();
        if (!isWriter) {
            throw new NoAuthority("스터디 신청서 승인");
        }

        study.addMember(member);
        study.getApplications().remove(this);
    }
}
