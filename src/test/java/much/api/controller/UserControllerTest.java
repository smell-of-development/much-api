package much.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import much.api.dto.request.JoinInformation;
import much.api.entity.SmsCertificationHist;
import much.api.entity.User;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

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
    }

    static class JoinRequestAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {

            return JoinInformation.builder()
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
    void user_join_test1(@AggregateWith(JoinRequestAggregator.class) JoinInformation information) throws Exception {
        // given
        String request = objectMapper.writeValueAsString(information);
        smsCertificationHistRepository.save(
                SmsCertificationHist.builder()
                        .phoneNumber(information.getPhoneNumber())
                        .certified(true)
                        .build());

        // expected
        mockMvc.perform(
                        post("/user")
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

        User user = userRepository.findByLoginId(information.getLoginId()).get();

        assertEquals(information.getLoginId(), user.getLoginId());
        assertTrue(passwordEncoder.matches(information.getPassword(), user.getPassword()));
        assertEquals(information.getNickname(), user.getNickname());
        assertEquals(information.getPhoneNumber(), user.getPhoneNumber());
        assertEquals(information.getPosition(), user.getPosition());
    }

    @ParameterizedTest(name = "[{index}] ID: {0}, PW: {1}, Nickname: {2}, PN: {3}, Pos: {4}")
    @DisplayName("사용자 등록 실패")
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
    void user_join_test2(@AggregateWith(JoinRequestAggregator.class) JoinInformation information) throws Exception {
        // given
        String request = objectMapper.writeValueAsString(information);

        // expected
        mockMvc.perform(
                        post("/user")
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