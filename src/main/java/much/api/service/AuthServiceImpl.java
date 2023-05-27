package much.api.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import much.api.common.enums.Code;
import much.api.common.properties.SmsProperties;
import much.api.common.util.PhoneNumberUtils;
import much.api.common.util.SmsSender;
import much.api.dto.response.SmsCertification;
import much.api.exception.NotMatchedPhoneNumberPatternException;
import much.api.exception.TokenRefreshBlockedUserException;
import much.api.exception.UserNotFound;
import much.api.common.util.TokenProvider;
import much.api.dto.Jwt;
import much.api.dto.response.Envelope;
import much.api.dto.response.OAuth2;
import much.api.entity.User;
import much.api.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static much.api.common.enums.Code.*;
import static much.api.common.properties.OAuth2Properties.*;
import static much.api.common.util.PhoneNumberUtils.*;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final WebClient webClient;

    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;

    private final SmsSender smsSender;

    private final SmsProperties smsProperties;


    /**
     * 액세스 토큰을 재발급한다.
     *
     * @param accessToken  유효기간만 만료된 정상 액세스토큰 OR 정상 액세스 토큰
     * @param refreshToken 정상 리프레시 토큰
     * @return 재발급 된 액세스 토큰 응답
     */

    public Envelope<Jwt> refreshAccessToken(final String accessToken,
                                            final String refreshToken) {

        // 유효한 리프레시 토큰이 아니라면 종료
        if (!tokenProvider.isValidRefreshToken(refreshToken)) {
            throw new InsufficientAuthenticationException("리프레시 토큰이 유효하지 않음");
        }

        // 유효한 리프레시 토큰의 유저 id (검증됨)
        final String userIdAtRefreshToken = tokenProvider.extractSubject(refreshToken);

        // 리프레시 가능한 액세스 토큰이 아니라면
        if (!isRefreshableAccessToken(accessToken, userIdAtRefreshToken)) {
            throw new InsufficientAuthenticationException("리프레시 불가");
        }

        // 사용자 조회
        User foundUser = userRepository.findById(Long.parseLong(userIdAtRefreshToken))
                .orElseThrow(() -> new UserNotFound(userIdAtRefreshToken));

        // 리프레시 가능 여부
        if (foundUser.getRefreshable()) {
            String newAccessToken = tokenProvider.createAccessToken(userIdAtRefreshToken, foundUser.getRole());
            return Envelope.ok(new Jwt(newAccessToken));
        }
        throw new TokenRefreshBlockedUserException(userIdAtRefreshToken);
    }


    /**
     * 리프레시 가능한 액세스 토큰인지 검사
     *
     * @param accessToken          액세스 토큰
     * @param userIdAtRefreshToken 리프레시 토큰의 user id
     * @return 리프레시 가능여부
     */
    private boolean isRefreshableAccessToken(final String accessToken,
                                             final String userIdAtRefreshToken) {

        // 액세스 토큰의 유저 id, role (미검증됨)
        final String userIdAtAccessToken = tokenProvider.extractSubject(accessToken);
        final String userRoleAtAccessToken = tokenProvider.extractClaim(accessToken, TokenProvider.CLAIM_ROLE);

        // 유효기간만 만료된 정상 발급되었던 액세스토큰이거나(액세스 토큰값은 이때 검증됨)
        // 유효한 액세스 토큰이면서
        // 같은 유저에 대한 토큰일 때
        return
                ((tokenProvider.isExpiredToken(accessToken) && userIdAtAccessToken != null && userRoleAtAccessToken != null)
                        || tokenProvider.isValidAccessToken(accessToken)
                ) && Objects.equals(userIdAtAccessToken, userIdAtRefreshToken);
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
    public Envelope<OAuth2> processOAuth2(final Provider providerInfo,
                                          final String code) {

        final OAuth2Token oAuth2Token = getOAuth2Token(providerInfo, code);
        final OpenId openId = getUserFromProvider(providerInfo, oAuth2Token);

        final String socialId = openId.getSub();
        final String serviceSocialId = providerInfo.makeUserSocialId(socialId);

        /*
        1. 소셜 ID로 기존 등록자 확인
         */
        Optional<User> optionalUser = userRepository.findBySocialId(serviceSocialId);

        if (optionalUser.isPresent()) {
            User joinedUser = optionalUser.get();

            // 정보 업데이트 (휴대폰 번호)
            toOnlyDigits(openId.getPhoneNumber())
                    .ifPresent(joinedUser::setPhoneNumber);

            return makeNewUserOrLoginSuccessResponse(joinedUser);
        }

        /*
        2. 신규 등록자
         */
        // 신규 등록자 공통 등록정보
        User.UserBuilder userBuilder = User.builder()
                .socialId(serviceSocialId)
                .picture(openId.getPicture())
                .email(openId.getEmail())
                .name(openId.getName());

        Optional<String> phoneNumberOptional = toOnlyDigits(openId.getPhoneNumber());

        // 2-1. 휴대폰번호 미존재 최초등록자.
        if (phoneNumberOptional.isEmpty()) {
            User newUser = userBuilder.build();
            User savedUser = userRepository.save(newUser);
            return makeNewUserResponse(savedUser);
        }

        // 휴대폰번호로 중복 사용자 조회
        final String phoneNumber = phoneNumberOptional.get();
        Optional<User> optionalDuplicatedUser = userRepository.findByPhoneNumber(phoneNumber);

        // 중복이 없다면
        if (optionalDuplicatedUser.isEmpty()) {
            // 2-2. 휴대폰번호 최초등록자
            User newUser = userBuilder.phoneNumber(phoneNumber).build();
            User savedUser = userRepository.save(newUser);
            return makeNewUserResponse(savedUser);
        }

        // 2-2. 휴대폰번호 중복 => 기존 사용자 정보로 진행
        User duplicatedUser = optionalDuplicatedUser.get();
        return makeNewUserOrLoginSuccessResponse(duplicatedUser);
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

        if (!PhoneNumberUtils.isOnlyDigitsPattern(phoneNumber)) {
            throw new NotMatchedPhoneNumberPatternException(phoneNumber);
        }

        int random = ThreadLocalRandom.current().nextInt(100_000, 1_000_000);
        boolean success = smsSender.sendSms(phoneNumber, String.valueOf(random));
        // TODO 레디스에 저장후 응답
        int expirationTimeInSeconds = smsProperties.getExpirationTimeInSeconds();
        if (success) {

        }

        return Envelope.ok(new SmsCertification(phoneNumber, expirationTimeInSeconds));
    }


    /**
     * 신규사용자 혹은 로그인 완료 응답
     *
     * @param user 유저 엔티티
     * @return 케이스별 응답객체
     */
    private Envelope<OAuth2> makeNewUserOrLoginSuccessResponse(User user) {
        // 필수정보 미입력 사용자 응답
        if (user.isNewUser()) {
            return makeNewUserResponse(user);
        }

        // 로그인 처리. 토큰 생성하여 응답
        return makeLoginSuccessResponse(user);
    }


    /**
     * 유저 정보로 액세스 토큰을 만들어 응답 code 200
     *
     * @param user 저장된 유저 엔티티
     * @return id, accessToken, refreshToken 가 설정된 응답객체
     */
    private Envelope<OAuth2> makeLoginSuccessResponse(final User user) {

        String id = user.getId().toString();

        String accessToken = tokenProvider.createAccessToken(id, user.getRole());
        String refreshToken = tokenProvider.createRefreshToken(id);

        return Envelope.ok(OAuth2.builder()
                .id(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
    }


    /**
     * 필수정보를 받아야 하는 유저의 응답을 생성 code 8100 또는 8101
     *
     * @param user 유저 엔티티
     * @return id 가 설정된 응답객체
     */
    private Envelope<OAuth2> makeNewUserResponse(final User user) {

        Code code = StringUtils.hasText(user.getPhoneNumber()) ?
                ADDITIONAL_INFORMATION_REQUIRED_1 : ADDITIONAL_INFORMATION_REQUIRED_2;

        return Envelope.okWithCode(code, OAuth2.builder()
                .id(user.getId())
                .build());
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

