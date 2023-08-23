package much.api.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import much.api.common.properties.SmsProperties;
import much.api.common.util.ContextUtils;
import much.api.common.util.PhoneNumberUtils;
import much.api.common.util.SmsSender;
import much.api.common.util.TokenProvider;
import much.api.dto.Jwt;
import much.api.dto.request.Login;
import much.api.dto.response.Envelope;
import much.api.dto.response.SmsCertification;
import much.api.entity.User;
import much.api.exception.*;
import much.api.repository.RedisRepository;
import much.api.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.String.valueOf;
import static much.api.common.enums.Code.*;
import static much.api.common.enums.RunMode.DEV;
import static much.api.common.properties.OAuth2Properties.*;
import static much.api.dto.response.Envelope.ok;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final WebClient webClient;

    private final UserRepository userRepository;

    private final RedisRepository redisRepository;

    private final TokenProvider tokenProvider;

    private final SmsSender smsSender;

    private final SmsProperties smsProperties;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Envelope<Jwt> login(Login loginRequest) {

        final String requestId = loginRequest.getLoginId();
        final String requestPassword = loginRequest.getPassword();

        User user = userRepository.findByLoginId(requestId)
                .orElseThrow(() -> new IncorrectLoginInfo(Long.valueOf(requestId)));

        if (!passwordEncoder.matches(requestPassword, user.getPassword())) {
            throw new IncorrectLoginInfo(Long.valueOf(requestId));
        }

        return Envelope.ok(tokenProvider.createTokenResponse(user.getId(), user.getRole()));
    }

    /**
     * 액세스 토큰을 재발급한다.
     *
     * @param accessToken  유효기간만 만료된 정상 액세스토큰 OR 정상 액세스 토큰
     * @param refreshToken 정상 리프레시 토큰
     * @return 재발급 된 액세스 토큰 응답
     */
    @Override
    public Envelope<Jwt> refreshAccessToken(final String accessToken,
                                            final String refreshToken) {

        final Long userId = tokenProvider.getSubject(accessToken);

        // 사용자 조회
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound(userId));

        // 사용자의 리프레시 가능 여부
        if (!foundUser.isRefreshable()) {
            throw new TokenRefreshBlocked(userId);
        }

        return ok(
                tokenProvider.checkRefreshableAndCreateToken(accessToken, refreshToken, foundUser.getRole())
        );
    }


    /**
     * OAuth2 로그인 과정을 진행
     *
     * @param providerInfo 로그인 한 제공자 정보
     * @param code         리다이렉트로 받은 Authorization Code
     * @return 각 로그인 케이스에 해당하는 응답
     */
    @Override
    @Transactional
    public Envelope<Jwt> processOAuth2(final Provider providerInfo,
                                       final String code) {

        final OAuth2Token oAuth2Token = getOAuth2Token(providerInfo, code);
        final OpenId openId = getUserFromProvider(providerInfo, oAuth2Token);

        final String socialId = openId.getSub();

        // TODO 연동된 계정에 대해서만 로그인 진행
        return null;
    }


    /**
     * 인증번호를 SMS 문자 발송을 통해 전송
     *
     * @param phoneNumber 수신 휴대폰번호
     * @return 성공시, 응답객체
     */
    @Override
    @Transactional
    public Envelope<SmsCertification> sendCertificationNumber(String phoneNumber) {

        // 휴대폰번호 형식 검사
        if (!PhoneNumberUtils.isOnlyDigitsPattern(phoneNumber)) {
            throw new InvalidPhoneNumber(phoneNumber);
        }

        // 개발환경 + 프로파일 smsPass 가 true 라면 바로 성공 응답
        if (ContextUtils.getRunMode().equals(DEV) && ContextUtils.isSmsPass()) {
            return Envelope.ok(new SmsCertification(phoneNumber, smsProperties.getExpirationTimeInMinutes()));
        }

        // 랜덤번호 채번
        int random = ThreadLocalRandom.current().nextInt(100_000, 1_000_000);
        String content = valueOf(random);

        // 문자발송
        boolean success = smsSender.sendSms(phoneNumber, content);

        // 레디스에 저장후 응답
        int expirationTimeInMinutes = smsProperties.getExpirationTimeInMinutes();
        if (success) {
            redisRepository.saveSmsCertificationNumber(phoneNumber, content, expirationTimeInMinutes);
            return ok(new SmsCertification(phoneNumber, expirationTimeInMinutes));
        }

        throw new MuchException(MESSAGE_SENDING_FAIL, "메세지 전송요청 실패");
    }


    /**
     * 전송된 인증번호를 확인
     *
     * @param phoneNumber         휴대폰 번호
     * @param certificationNumber 인증번호
     */
    @Override
    @Transactional
    public void verifyCertificationNumber(String phoneNumber, String certificationNumber) {

        if (!PhoneNumberUtils.isOnlyDigitsPattern(phoneNumber)) {
            throw new InvalidPhoneNumber(phoneNumber);
        }

        // 개발환경 + 프로파일 smsPass 가 true 라면 바로 성공 응답
        if (ContextUtils.getRunMode().equals(DEV) && ContextUtils.isSmsPass()) {
            return;
        }

        // 전송기록 찾기
        String savedCertificationNumber = redisRepository.findSmsCertificationNumber(phoneNumber);
        if (savedCertificationNumber == null) {
            throw new MuchException(SENDING_HISTORY_NOT_FOUND, String.format("[%s] 발송기록 미존재", phoneNumber));
        }

        log.info("저장된 번호: {}, 인증번호: {}, 입력 인증번호: {}", phoneNumber, savedCertificationNumber, certificationNumber);

        // 인증번호 일치
        if (savedCertificationNumber.equals(certificationNumber)) {

            // 확인한 번호 삭제
            redisRepository.removeSmsCertificationNumber(phoneNumber);
            return;
        }

        // 불일치
        throw new MuchException(CERTIFICATION_NUMBER_NOT_MATCHED, "인증번호 불일치");
    }


    /**
     * 액세스 토큰을 사용하여 유저 정보를 얻어온다.
     *
     * @param providerInfo OAuth2 제공자 정보
     * @param oAuth2Token  받아온 토큰객체
     * @return 제공자로부터 얻어온 OpenId 객체
     */
    private OpenId getUserFromProvider(final Provider providerInfo,
                                       final OAuth2Token oAuth2Token) {

        return webClient.get()
                .uri(providerInfo.getUserInfoUri())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + oAuth2Token.getAccessToken())
                .retrieve()
                .bodyToMono(OpenId.class)
                .block();
    }


    /**
     * authorization code 를 이용해 액세스 토큰을 얻어온다.
     *
     * @param providerInfo OAuth2 제공자 정보
     * @param code         authorization code
     * @return 제공자로부터 얻어온 토큰정보 객체
     */
    private OAuth2Token getOAuth2Token(final Provider providerInfo,
                                       final String code) {

        return webClient.post()
                .uri(fromHttpUrl(providerInfo.getTokenUri())
                        .queryParam(CODE_KEY, code)
                        .queryParam(CLIENT_ID_KEY, providerInfo.getClientId())
                        .queryParam(CLIENT_SECRET_KEY, providerInfo.getClientSecret())
                        .queryParam(GRANT_TYPE_KEY, providerInfo.getAuthorizationGrantType())
                        .queryParam(REDIRECT_URI_KEY, providerInfo.getRedirectUri())
                        .toUriString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .retrieve()
                .bodyToMono(OAuth2Token.class)
                .block();
    }


    @Getter
    private static class OpenId {

        private String sub;

        private String name;

        private String picture;

        private String email;

        @JsonProperty("email_verified")
        private Boolean emailVerified;

        @JsonProperty("phone_number")
        private String phoneNumber;

        @JsonProperty("phone_number_verified")
        private Boolean phoneNumberVerified;

    }


    @Getter
    private static class OAuth2Token {

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("id_token")
        private String idToken;

        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("expires_in")
        private Integer expiresIn;

        @JsonProperty("refresh_token")
        private String refreshToken;

        private String scope;

    }


}

