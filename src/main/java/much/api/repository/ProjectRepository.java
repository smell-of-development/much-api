package much.api.repository;

import much.api.common.enums.MuchType;
import much.api.entity.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @EntityGraph(attributePaths = {"writer"})
    Optional<Project> findByIdAndType(Long id, MuchType type);

}
