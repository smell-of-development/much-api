package much.api.repository;

import much.api.entity.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @NonNull
    @Override
    @EntityGraph(attributePaths = {"writer"})
    Optional<Project> findById(@NonNull Long id);

}
