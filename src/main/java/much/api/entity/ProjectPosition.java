package much.api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import much.api.common.exception.NeedsLessThanRecruitedPosition;
import much.api.common.util.ContextUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "tb_project_position",
        indexes = {
                @Index(name = "tb_project_position_idx1", columnList = "project_id"),
        }
)
public class ProjectPosition extends BaseTimeEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter

    @JoinColumn(name = "project_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @Getter
    private String name;

    @Getter
    private int needs;

    @Getter
    private int recruited;

    @Getter
    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectJoin> positionJoins = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectApplication> positionApplications = new ArrayList<>();

    @Transient
    private boolean containsMe;

    @Transient
    private boolean deletable;

    @Builder
    private ProjectPosition(Project project,
                            String name,
                            int needs) {

        this.project = project;
        this.name = name;
        this.needs = needs;
    }


    public boolean closed() {

        return needs <= recruited;
    }


    /**
     * 프로젝트의 해당 포지션에 조회자가 존재하는지 확인
     *
     * @return 조회자의 포지션 해당 여부
     */
    public boolean isContainsMe() {

        if (project.isWriter()) {
            positionJoins.stream()
                    .filter(join -> join.getMember().getId().equals(ContextUtils.getUserId()))
                    .findAny()
                    .ifPresent((pj) -> containsMe = true);
        }

        return containsMe;
    }


    /**
     * 조회자가 프로젝트 작성자이면서 포지션에 혼자만 존재하거나,
     * 아무도 존재하지 않는지 확인
     *
     * @return 포지션 삭제 가능 여부
     */
    public boolean isDeletable() {

        if (!project.isWriter()) {
            return false;
        }

        return (isContainsMe() && recruited == 1) || recruited == 0;
    }


    public void addPositionJoin(ProjectJoin projectJoin) {

        positionJoins.add(projectJoin);
        recruited = positionJoins.size();
    }


    public void deleteJoin(Collection<ProjectJoin> joins) {

        positionJoins.removeAll(joins);
    }


    public void modify(String name, int needs, boolean containsWriter) {

        if (containsWriter) {
            User writer = project.getWriter();

            // 작성자 수정 기존 포지션 == 기존 작성자 포지션 -> 유지
            boolean positionMaintained = positionJoins.stream()
                    .anyMatch(pj -> pj.getMember().getId().equals(writer.getId()));

            // 작성자 기존 포지션 중 이동 -> 처리
            if (!positionMaintained) {
                // 기존 작성자 포지션 등록을 제거
                project.getProjectJoins().stream()
                        .filter(pj -> pj.getMember().getId().equals(writer.getId()))
                        .findAny()
                        .ifPresent(pj -> pj.getPosition().deleteJoin(List.of(pj)));

                // 수정 기존 포지션 등록
                addPositionJoin(
                        ProjectJoin.builder()
                                .project(project)
                                .position(this)
                                .member(writer)
                                .build()
                );
            }
        }

        if (needs < recruited) {
            throw new NeedsLessThanRecruitedPosition(name, recruited);
        }

        this.name = name;
        this.needs = needs;
    }

}
