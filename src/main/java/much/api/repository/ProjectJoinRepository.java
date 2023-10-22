package much.api.repository;

import much.api.entity.ProjectJoin;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectJoinRepository extends JpaRepository<ProjectJoin, Long> {

    @EntityGraph(attributePaths = {"project", "position", "member"})
    Optional<ProjectJoin> findByProjectIdAndMemberId(Long projectId, Long memberId);

}
