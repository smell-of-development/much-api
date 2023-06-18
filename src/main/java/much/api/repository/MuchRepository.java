package much.api.repository;

import much.api.entity.Much;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MuchRepository extends JpaRepository<Much, Long> {
}
