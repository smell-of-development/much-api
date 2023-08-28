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
import much.api.dto.request.Login;
import much.api.dto.response.Envelope;
import much.api.dto.response.SmsCertification;
import much.api.dto.response.WebToken;
import much.api.entity.SmsCertificationHist;
import much.api.entity.User;
import much.api.exception.*;
import much.api.repository.SmsCertificationHistRepository;
import much.api.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import static java.time.LocalDateTime.now;
import static much.api.common.properties.OAuth2Properties.*;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final WebClient webClient;

    private final CommonService commonService;

    private final UserRepository userRepository;

    private final SmsCertificationHistRepository smsCertificationHistRepository;

    private final TokenProvider tokenProvider;

    private final SmsSender smsSender;

    private final SmsProperties smsProperties;

    private final PasswordEncoder passwordEncoder;


    public WebToken login(Login loginRequest) {

        final String requestId = loginRequest.getLoginId();
        final String requestPassword = loginRequest.getPassword();

        User user = userRepository.findByLoginId(requestId)
                .orElseThrow(() -> new IncorrectLoginInfo(Long.valueOf(requestId)));

        if (!passwordEncoder.matches(requestPassword, user.getPassword())) {
            throw new IncorrectLoginInfo(Long.valueOf(requestId));
        }

        return WebToken.ofJwt(
                tokenProvider.createTokenResponse(user.getId(), user.getRole())
        );
    }


    /**
     * 액세스 토큰을 재발급한다.
     *
     * @param accessToken  유효기간만 만료된 정상 액세스토큰 OR 정상 액세스 토큰
     * @param refreshToken 정상 리프레시 토큰
     * @return 재발급 된 액세스 토큰 응답
     */
    public WebToken refreshAccessToken(final String accessToken,
                                       final String refreshToken) {

        final Long userId = tokenProvider.getSubject(accessToken);

        // 사용자 조회
        User foundUser = commonService.getUserOrThrowException(userId);

        // 사용자의 리프레시 가능 여부
        if (!foundUser.isRefreshable()) {
            throw new TokenRefreshBlocked(userId);
        }

        return WebToken.ofJwt(
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
    @Transactional
    public Envelope<WebToken> processOAuth2(final Provider providerInfo,
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
    @Transactional
    public SmsCertification sendCertificationNumber(String phoneNumber) {

        final int expirationTimeInMinutes = smsProperties.getExpirationTimeInMinutes();

        // 휴대폰번호 형식 검사
        if (!PhoneNumberUtils.isOnlyDigitsPattern(phoneNumber)) {
            throw new InvalidPhoneNumber(phoneNumber);
        }
        // 개발환경 + 프로파일 smsPass 가 true 라면 바로 성공 응답
        if (ContextUtils.isSmsPass()) {

            return SmsCertification.builder()
                    .phoneNumber(phoneNumber)
                    .remainTimeInMinutes(expirationTimeInMinutes)
                    .build();
        }

        // 하루 최대 전송횟수 초과 검사
        LocalDateTime previousDateTime = now().minusDays(1L);
        if (smsCertificationHistRepository
                .existsHistMoreThanN(
                        phoneNumber,
                        previousDateTime,
                        smsProperties.getMaxSendingCountPerDay())) {

            throw new CertificationMessageSendingCountExceeded(phoneNumber);
        }

        // 랜덤번호 채번
        int random = ThreadLocalRandom.current().nextInt(100_000, 1_000_000);
        String randomToString = String.valueOf(random);
        String content = String.format(smsProperties.getCertificationMessageFormat(), randomToString);

        // 문자발송
        boolean success = smsSender.sendSms(phoneNumber, content);

        // DB 저장후 응답
        if (success) {

            SmsCertificationHist hist = SmsCertificationHist.builder()
                    .phoneNumber(phoneNumber)
                    .number(randomToString)
                    .build();

            smsCertificationHistRepository.save(hist);

            return SmsCertification.builder()
                    .phoneNumber(phoneNumber)
                    .remainTimeInMinutes(expirationTimeInMinutes)
                    .build();
        }

        throw new MessageSendingFail(phoneNumber);
    }


    /**
     * 전송된 인증번호를 확인
     *
     * @param phoneNumber         휴대폰 번호
     * @param certificationNumber 인증번호
     */
    @Transactional
    public void verifyCertificationNumber(String phoneNumber, String certificationNumber) {

        if (!PhoneNumberUtils.isOnlyDigitsPattern(phoneNumber)) {
            throw new InvalidPhoneNumber(phoneNumber);
        }
        // 개발환경 + 프로파일 smsPass 가 true 라면 바로 성공 응답
        if (ContextUtils.isSmsPass()) {
            return;
        }

        // 유효시간 내 전송기록 찾기
        LocalDateTime after = now().minusMinutes(smsProperties.getExpirationTimeInMinutes());

        SmsCertificationHist hist = smsCertificationHistRepository.findLatestSent(phoneNumber, after)
                .orElseThrow(() -> new CertificationNumberSendingNeeded(phoneNumber));

        String savedCertificationNumber = hist.getNumber();

        log.info("휴대폰번호: {}, 인증번호: {}, 입력 인증번호: {}", phoneNumber, savedCertificationNumber, certificationNumber);

        // 인증번호 일치
        if (!savedCertificationNumber.equals(certificationNumber)) {
            throw new CertificationNumberNotMatched(certificationNumber);
        }

        // 완료
        hist.certify();
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

