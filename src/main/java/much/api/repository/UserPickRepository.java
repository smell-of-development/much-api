package much.api.repository;

import much.api.common.enums.MuchType;
import much.api.entity.User;
import much.api.entity.UserPick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserPickRepository extends JpaRepository<UserPick, Long> {

    @Query("SELECT up " +
            "FROM UserPick up " +
            "WHERE up.user = :user " +
            "AND   up.targetType = :targetType " +
            "AND   up.targetId = :targetId")
    Optional<UserPick> findUserPick(@Param("user") User user,
                                    @Param("targetType") MuchType targetType,
                                    @Param("targetId") Long targetId);

}
