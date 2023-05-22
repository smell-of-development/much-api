package much.api.repository;

import much.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByKakaoId(String kakaoId);

    Optional<User> findByGoogleId(String googleId);

    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByNickname(String nickname);

}
