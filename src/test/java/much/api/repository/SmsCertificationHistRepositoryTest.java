package much.api.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import much.api.entity.SmsCertificationHist;
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
class SmsCertificationHistRepositoryTest {

    @Autowired
    SmsCertificationHistRepository smsCertificationHistRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @DisplayName("최근 하루 5건 이상 조회 테스트 - 방금 4건 전송")
    void test1() {

        // given
        String phoneNumber = "01012345678";

        IntStream.range(0, 4)
                .forEach(i -> smsCertificationHistRepository.save(SmsCertificationHist.builder()
                        .number("123456")
                        .phoneNumber(phoneNumber)
                        .build()));

        // when
        boolean result = smsCertificationHistRepository.existsHistMoreThanN(phoneNumber, now().minusDays(1L), 5);

        // then
        assertEquals(4L, smsCertificationHistRepository.count());
        assertFalse(result);
    }

    @Test
    @DisplayName("최근 하루 5건 이상 조회 테스트 - 방금 5건 전송")
    void test2() {

        // given
        String phoneNumber = "01012345678";

        IntStream.range(0, 5)
                .forEach(i -> smsCertificationHistRepository.save(SmsCertificationHist.builder()
                        .number("123456")
                        .phoneNumber(phoneNumber)
                        .build()));

        // when
        boolean result = smsCertificationHistRepository.existsHistMoreThanN(phoneNumber, now().minusDays(1L), 5);

        // then
        assertEquals(5L, smsCertificationHistRepository.count());
        assertTrue(result);
    }


    @Test
    @DisplayName("최근 하루 5건 이상 조회 테스트 - 23시간59분 전 5건 전송")
    void test3() {

        // given
        String phoneNumber = "01012345678";

        IntStream.range(0, 5)
                .forEach(i -> {
                    SmsCertificationHist hist = SmsCertificationHist.builder()
                            .number("123456")
                            .phoneNumber(phoneNumber)
                            .build();


                    smsCertificationHistRepository.save(hist);
                });

        Query query = entityManager.createQuery("UPDATE SmsCertificationHist sch SET sch.createdAt = :createdAt");

        LocalDateTime minusDays = now().minusHours(23).minusMinutes(59);
        log.info(minusDays.toString());
        query.setParameter("createdAt", minusDays)
                .executeUpdate();

        entityManager.clear();

        // when
        boolean result = smsCertificationHistRepository.existsHistMoreThanN(phoneNumber, now().minusDays(1L), 5);

        // then
        assertEquals(5L, smsCertificationHistRepository.count());
        assertTrue(result);
    }


    @Test
    @DisplayName("최근 하루 5건 이상 조회 테스트 - 하루전 5건 전송")
    void test4() {

        // given
        String phoneNumber = "01012345678";

        IntStream.range(0, 5)
                .forEach(i -> {
                    SmsCertificationHist hist = SmsCertificationHist.builder()
                            .number("123456")
                            .phoneNumber(phoneNumber)
                            .build();


                    smsCertificationHistRepository.save(hist);
                });

        Query query = entityManager.createQuery("UPDATE SmsCertificationHist sch SET sch.createdAt = :createdAt");

        LocalDateTime minusDays = now().minusDays(1L);
        log.info(minusDays.toString());
        query.setParameter("createdAt", minusDays)
                .executeUpdate();

        entityManager.clear();

        // when
        boolean result = smsCertificationHistRepository.existsHistMoreThanN(phoneNumber, now().minusDays(1L), 5);

        // then
        assertEquals(5L, smsCertificationHistRepository.count());
        assertFalse(result);
    }

}