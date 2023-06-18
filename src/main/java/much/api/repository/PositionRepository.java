package much.api.repository;

import much.api.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Integer> {

    @Query("SELECT p FROM Position p WHERE p.code = :code AND p.code BETWEEN 100 AND 999")
    Optional<Position> findByJogGroupCode(Integer code);

    @Query("SELECT p FROM Position p WHERE p.code = :code AND p.code BETWEEN 1000 AND 9999")
    Optional<Position> findByCareerCode(Integer code);

    List<Position> findAllByCodeBetween(Integer from, Integer to);

}
