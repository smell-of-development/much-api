package much.api.repository;

import much.api.entity.ProjectPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectPositionRepository extends JpaRepository<ProjectPosition, Long> {

    @Query("SELECT pp " +
            "FROM ProjectPosition pp " +
            "JOIN FETCH pp.project p " +
            "WHERE p.id IN (:projectIds)")
    List<ProjectPosition> findAllByProjectIdIn(@Param("projectIds") List<Long> projectIds);

}
