package much.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import much.api.dto.request.UserCreation;
import much.api.entity.SmsCertificationHist;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerV1Test {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SmsCertificationHistRepository smsCertificationHistRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @BeforeEach
    void clean() {
        userRepository.deleteAll();
        smsCertificationHistRepository.deleteAll();
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
    void user_join_test1(@AggregateWith(UserCreationAggregator.class) UserCreation information) throws Exception {
        // given
        String request = objectMapper.writeValueAsString(information);
        smsCertificationHistRepository.save(
                SmsCertificationHist.builder()
                        .phoneNumber(information.getPhoneNumber())
                        .certified(true)
                        .build());

        // expected
        mockMvc.perform(
                        post("/api/v1/user")
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.requires").isEmpty())
                .andExpect(jsonPath("$.result.id").isNotEmpty())
                .andExpect(jsonPath("$.result.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.result.refreshToken").isNotEmpty());
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
    void user_join_test2(@AggregateWith(UserCreationAggregator.class) UserCreation information) throws Exception {
        // given
        String request = objectMapper.writeValueAsString(information);

        // expected
        mockMvc.perform(
                        post("/api/v1/user")
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(2000))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.requires").isEmpty())
                .andExpect(jsonPath("$.result").isEmpty());
    }

    @ParameterizedTest(name = "[{index}] ID: {0}, PW: {1}, Nickname: {2}, PN: {3}, Pos: {4}")
    @DisplayName("사용자 등록 실패 - 휴대폰인증 안함")
    @CsvSource({
            "testId1, testPassword1, test1, 01012341234, 백엔드",
            "muchTest, muchTest, much, 01011112222,"
    })
    void user_join_test3(@AggregateWith(UserCreationAggregator.class) UserCreation information) throws Exception {
        // given
        String request = objectMapper.writeValueAsString(information);

        // expected
        mockMvc.perform(
                        post("/api/v1/user")
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(2000))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.requires").isEmpty())
                .andExpect(jsonPath("$.result").isEmpty());
    }
}