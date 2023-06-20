package much.api.repository;

import much.api.common.enums.MuchType;
import much.api.entity.Much;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MuchRepository extends JpaRepository<Much, Long> {

    @EntityGraph(attributePaths = {"writer"})
    Optional<Much> findByIdAndType(Long id, MuchType type);

}
