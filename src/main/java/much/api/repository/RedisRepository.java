package much.api.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final StringRedisTemplate stringRedisTemplate;


    public void saveSmsCertificationNumber(final String phoneNumber,
                                           final String certificationNumber,
                                           final int expirationTimeInMinutes) {

        stringRedisTemplate.opsForValue()
                .set(phoneNumber, certificationNumber, Duration.ofSeconds(expirationTimeInMinutes * 60L));
    }


    public String findSmsCertificationNumber(final String phoneNumber) {

        return stringRedisTemplate.opsForValue().get(phoneNumber);
    }


    public void removeSmsCertificationNumber(final String phoneNumber) {

        stringRedisTemplate.delete(phoneNumber);
    }

}
