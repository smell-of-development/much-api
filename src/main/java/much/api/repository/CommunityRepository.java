package much.api.repository;

import much.api.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    @Query("SELECT c " +
            "FROM Community c " +
            "JOIN FETCH c.author " +
            "WHERE c.id = :id ")
    Optional<Community> findPostWithUser(@Param("id") Long id);

}
