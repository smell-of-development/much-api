package much.api.repository;

import much.api.entity.Study;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long> {

    @Override
    @EntityGraph(attributePaths = {"writer"})
    Optional<Study> findById(@NonNull Long id);
}
