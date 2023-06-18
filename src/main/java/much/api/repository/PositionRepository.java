package much.api.repository;

import much.api.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Integer> {

    List<Position> findAllByCodeBetween(Integer from, Integer to);

    List<Position> findByCodeIn(List<Integer> codes);

}
