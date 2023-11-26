package much.api.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import much.api.entity.SmsVerificationHist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@DataJpaTest
class SmsValidationHistRepositoryTest {

    @Autowired
    SmsVerificationHistRepository smsVerificationHistRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @DisplayName("최근 하루 5건 이상 조회 테스트 - 방금 4건 전송")
    void test1() {

        // given
        String phoneNumber = "01012345678";

        IntStream.range(0, 4)
                .forEach(i -> smsVerificationHistRepository.save(SmsVerificationHist.builder()
                        .number("123456")
                        .phoneNumber(phoneNumber)
                        .build()));

        // when
        boolean result = smsVerificationHistRepository.existsHistMoreThanN(phoneNumber, now().minusDays(1L), 5);

        // then
        assertEquals(4L, smsVerificationHistRepository.count());
        assertFalse(result);
    }

    @Test
    @DisplayName("최근 하루 5건 이상 조회 테스트 - 방금 5건 전송")
    void test2() {

        // given
        String phoneNumber = "01012345678";

        IntStream.range(0, 5)
                .forEach(i -> smsVerificationHistRepository.save(SmsVerificationHist.builder()
                        .number("123456")
                        .phoneNumber(phoneNumber)
                        .build()));

        // when
        boolean result = smsVerificationHistRepository.existsHistMoreThanN(phoneNumber, now().minusDays(1L), 5);

        // then
        assertEquals(5L, smsVerificationHistRepository.count());
        assertTrue(result);
    }


    @Test
    @DisplayName("최근 하루 5건 이상 조회 테스트 - 23시간59분 전 5건 전송")
    void test3() {

        // given
        String phoneNumber = "01012345678";

        IntStream.range(0, 5)
                .forEach(i -> {
                    SmsVerificationHist hist = SmsVerificationHist.builder()
                            .number("123456")
                            .phoneNumber(phoneNumber)
                            .build();


                    smsVerificationHistRepository.save(hist);
                });

        Query query = entityManager.createQuery("UPDATE SmsVerificationHist sch SET sch.createdAt = :createdAt");

        LocalDateTime minusDays = now().minusHours(23).minusMinutes(59);
        log.info(minusDays.toString());
        query.setParameter("createdAt", minusDays)
                .executeUpdate();

        entityManager.clear();

        // when
        boolean result = smsVerificationHistRepository.existsHistMoreThanN(phoneNumber, now().minusDays(1L), 5);

        // then
        assertEquals(5L, smsVerificationHistRepository.count());
        assertTrue(result);
    }


    @Test
    @DisplayName("최근 하루 5건 이상 조회 테스트 - 하루전 5건 전송")
    void test4() {

        // given
        String phoneNumber = "01012345678";

        IntStream.range(0, 5)
                .forEach(i -> {
                    SmsVerificationHist hist = SmsVerificationHist.builder()
                            .number("123456")
                            .phoneNumber(phoneNumber)
                            .build();


                    smsVerificationHistRepository.save(hist);
                });

        Query query = entityManager.createQuery("UPDATE SmsVerificationHist sch SET sch.createdAt = :createdAt");

        LocalDateTime minusDays = now().minusDays(1L);
        log.info(minusDays.toString());
        query.setParameter("createdAt", minusDays)
                .executeUpdate();

        entityManager.clear();

        // when
        boolean result = smsVerificationHistRepository.existsHistMoreThanN(phoneNumber, now().minusDays(1L), 5);

        // then
        assertEquals(5L, smsVerificationHistRepository.count());
        assertFalse(result);
    }

}