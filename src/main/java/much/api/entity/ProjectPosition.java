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
        name = "tb_project_position",
        indexes = {
                @Index(name = "tb_project_position_idx1", columnList = "project_id, name", unique = true),
        }
)
public class ProjectPosition extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "project_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    private String name;

    private int needs;

    private int recruited;

    @Builder
    private ProjectPosition(Project project, String name,
                            int needs, int recruited) {

        this.project = project;
        this.name = name;
        this.needs = needs;
        this.recruited = recruited;
    }


    public boolean isClosed() {

        return needs == recruited;
    }
}
