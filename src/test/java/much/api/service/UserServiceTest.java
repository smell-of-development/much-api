package much.api.service;

import much.api.dto.request.UserCreation;
import much.api.dto.response.WebToken;
import much.api.entity.SmsCertificationHist;
import much.api.entity.User;
import much.api.common.exception.CertificationNeeded;
import much.api.common.exception.MuchException;
import much.api.repository.SmsCertificationHistRepository;
import much.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    SmsCertificationHistRepository smsCertificationHistRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @BeforeEach
    void clean() {
        smsCertificationHistRepository.deleteAll();
        userRepository.deleteAll();
    }

    static class UserCreationAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {

            return UserCreation.builder()
                    .loginId(accessor.getString(0))
                    .password(accessor.getString(1))
                    .nickname(accessor.getString(2))
                    .phoneNumber(accessor.getString(3))
                    .position(accessor.getString(4))
                    .build();
        }
    }

    @ParameterizedTest(name = "[{index}] ID: {0}, PW: {1}, Nickname: {2}, PN: {3}, Pos: {4}")
    @DisplayName("사용자 등록 성공")
    @CsvSource({
            "testId1, testPassword1, test1, 01012341234, 백엔드",
            "muchTest, muchTest, much, 01011112222,"
    })
    void user_join_test1(@AggregateWith(UserCreationAggregator.class) UserCreation userCreation) {
        // given
        smsCertificationHistRepository.save(
                SmsCertificationHist.builder()
                        .phoneNumber(userCreation.getPhoneNumber())
                        .certified(true)
                        .build());

        // when
        WebToken webToken = userService.createUser(userCreation);

        // then
        User user = userRepository.findByLoginId(userCreation.getLoginId()).get();

        assertEquals(user.getId(), webToken.getId());
        assertNotNull(webToken.getAccessToken());
        assertNotNull(webToken.getRefreshToken());
        assertEquals(userCreation.getLoginId(), user.getLoginId());
        assertTrue(passwordEncoder.matches(userCreation.getPassword(), user.getPassword()));
        assertEquals(userCreation.getNickname(), user.getNickname());
        assertEquals(userCreation.getPhoneNumber(), user.getPhoneNumber());
        assertEquals(userCreation.getPosition(), user.getPosition());
    }

    @ParameterizedTest(name = "[{index}] ID: {0}, PW: {1}, Nickname: {2}, PN: {3}, Pos: {4}")
    @DisplayName("사용자 등록 실패 - 입력형식 불일치")
    @CsvSource({
            "'' , testPassword, test, 01012341234, 백엔드",
            "veryVeryVeryLongLongLongId, testPassword, test, 01012341234, 백엔드",

            "muchTest, '', much, 01011112222, 프론트엔드",
            "muchTest, veryVeryVeryLongLongLongPassword, much, 01011112222, 프론트엔드",

            "muchTest, testPassword, '', 01011112222, 백엔드",
            "muchTest, testPassword, veryVeryVeryLongLongLongNickname, 01011112222, 백엔드",

            "muchTest, testPassword, much, '', 백엔드",
            "muchTest, testPassword, much, 010-1111-2222, 백엔드",
    })
    void user_join_test2(@AggregateWith(UserCreationAggregator.class) UserCreation userCreation) {
        // expected
        assertThrows(MuchException.class, () -> userService.createUser(userCreation));
    }

    @ParameterizedTest(name = "[{index}] ID: {0}, PW: {1}, Nickname: {2}, PN: {3}, Pos: {4}")
    @DisplayName("사용자 등록 실패 - 휴대폰인증 안함")
    @CsvSource({
            "testId1, testPassword1, test1, 01012341234, 백엔드",
            "muchTest, muchTest, much, 01011112222,"
    })
    void user_join_test3(@AggregateWith(UserCreationAggregator.class) UserCreation userCreation) {
        // expected
        assertThrows(CertificationNeeded.class, () -> userService.createUser(userCreation));
    }
}