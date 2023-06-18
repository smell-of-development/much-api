package much.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import much.api.common.enums.Code;
import much.api.common.enums.Role;
import much.api.common.enums.RunMode;
import much.api.common.util.ContextUtils;
import much.api.common.util.TokenProvider;
import much.api.dto.request.Login;
import much.api.dto.request.SmsVerification;
import much.api.dto.response.SmsCertification;
import much.api.common.properties.OAuth2Properties;
import much.api.controller.swagger.AuthApi;
import much.api.dto.Jwt;
import much.api.dto.response.Envelope;
import much.api.dto.response.OAuth2Uri;
import much.api.entity.User;
import much.api.exception.BusinessException;
import much.api.service.AuthService;
import much.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    private final UserService userService;

    private final OAuth2Properties oAuth2Properties;

    private final TokenProvider tokenProvider;

    @Override
    @GetMapping("/testToken")
    public ResponseEntity<Envelope<Jwt>> testToken(@RequestParam(defaultValue = "0") String id) {

        if (!ContextUtils.getRunMode().equals(RunMode.DEV)) {
            throw new AccessDeniedException("개발모드가 아님");
        }

        Jwt response = tokenProvider.createTokenResponse(id, Role.ROLE_USER);
        return ResponseEntity.ok(Envelope.ok(response));
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<Envelope<Jwt>> login(@RequestBody @Valid Login request) {

        return ResponseEntity.ok(authService.login(request));
    }

    @Override
//    @GetMapping("/oauth2/authorization/{provider}")
    public ResponseEntity<Envelope<OAuth2Uri>> retrieveOAuth2Uri(@PathVariable String provider) {

        return ResponseEntity.ok(
                Envelope.ok(oAuth2Properties
                        .findProviderWithName(provider)
                        .makeOAuth2UriResponse()
                ));
    }


    @Override
//    @PostMapping("/oauth2/code/{provider}")
    public ResponseEntity<Envelope<Jwt>> handleOAuth2(@PathVariable String provider,
                                                        @RequestParam String code) {

        OAuth2Properties.Provider providerInfo = oAuth2Properties.findProviderWithName(provider);

        String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);
        Envelope<Jwt> oAuth2Response = authService.processOAuth2(providerInfo, decodedCode);

        return ResponseEntity.ok(oAuth2Response);
    }


    @Override
    @PostMapping("/auth/refresh")
    public ResponseEntity<Envelope<Jwt>> refreshAccessToken(@RequestBody Jwt request) {

        final String accessToken = request.getAccessToken();
        final String refreshToken = request.getRefreshToken();

        return ResponseEntity.ok(authService.refreshAccessToken(accessToken, refreshToken));
    }


    @Override
    @GetMapping("/check")
    public ResponseEntity<Envelope<Long>> checkToken() {

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(Envelope.ok(Long.parseLong(principal.getUsername())));
    }


    @Override
    @PostMapping("/sms/join-certification")
    public ResponseEntity<Envelope<SmsCertification>> sendJoinCertificationNumber(@RequestParam String phoneNumber) {

        Optional<User> userByPhoneNumber = userService.findUserByPhoneNumber(phoneNumber);
        if (userByPhoneNumber.isPresent()) {
            throw new BusinessException(Code.DUPLICATED_PHONE_NUMBER, String.format("휴대폰번호 중복. [%s]", phoneNumber));
        }

        Envelope<SmsCertification> response = authService.sendCertificationNumber(phoneNumber);
        return ResponseEntity.ok(response);
    }


    @Override
    @PostMapping("/sms/join-verification")
    public ResponseEntity<Envelope<Void>> verifyJoinCertificationNumber(@RequestBody @Valid SmsVerification request) {

        final String phoneNumber = request.getPhoneNumber();
        final String certificationNumber = request.getCertificationNumber();

        authService.verifyCertificationNumber(phoneNumber, certificationNumber);
        return ResponseEntity.ok(Envelope.empty());
    }



}
