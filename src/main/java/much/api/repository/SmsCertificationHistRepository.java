package much.api.repository;

import much.api.entity.SmsCertificationHist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SmsCertificationHistRepository extends JpaRepository<SmsCertificationHist, Long> {


    @Query("SELECT COUNT(sch) >= :n " +
            "FROM SmsCertificationHist sch " +
            "WHERE sch.phoneNumber = :phoneNumber " +
            "AND sch.createdAt >= :after")
    boolean existsHistMoreThanN(@Param("phoneNumber") String phoneNumber,
                                @Param("after") LocalDateTime after,
                                @Param("n") int n);

    @Query("SELECT sch " +
            "FROM SmsCertificationHist sch " +
            "WHERE sch.phoneNumber = :phoneNumber " +
            "AND sch.updatedAt >= :after " +
            "ORDER BY sch.updatedAt DESC " +
            "LIMIT 1")
    Optional<SmsCertificationHist> findLatestSent(@Param("phoneNumber") String phoneNumber,
                                                  @Param("after") LocalDateTime after);
}
