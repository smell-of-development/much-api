package much.api.repository;

import much.api.entity.SmsVerificationHist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SmsVerificationHistRepository extends JpaRepository<SmsVerificationHist, Long> {


    @Query("SELECT COUNT(svh) >= :n " +
            "FROM SmsVerificationHist svh " +
            "WHERE svh.phoneNumber = :phoneNumber " +
            "AND svh.createdAt >= :after")
    boolean existsHistMoreThanN(@Param("phoneNumber") String phoneNumber,
                                @Param("after") LocalDateTime after,
                                @Param("n") int n);

    @Query("SELECT svh " +
            "FROM SmsVerificationHist svh " +
            "WHERE svh.phoneNumber = :phoneNumber " +
            "AND svh.updatedAt >= :after " +
            "ORDER BY svh.updatedAt DESC " +
            "LIMIT 1")
    Optional<SmsVerificationHist> findLatestSent(@Param("phoneNumber") String phoneNumber,
                                                 @Param("after") LocalDateTime after);
}
