package much.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import much.api.common.enums.Role;
import much.api.common.util.ContextUtils;
import much.api.common.util.TokenProvider;
import much.api.controller.swagger.AuthApiV1;
import much.api.dto.request.Login;
import much.api.dto.request.SmsVerification;
import much.api.dto.response.Envelope;
import much.api.dto.response.SmsCertification;
import much.api.dto.response.WebToken;
import much.api.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthControllerV1 implements AuthApiV1 {

    private final AuthService authService;

    private final TokenProvider tokenProvider;

    @Override
    @GetMapping("/testToken")
    public ResponseEntity<Envelope<WebToken>> testToken(@RequestParam long id) {

        if (ContextUtils.isProdMode()) {
            throw new AccessDeniedException("개발모드가 아님");
        }

        TokenProvider.Jwt jwt = tokenProvider.createTokenResponse(id, Role.ROLE_USER);

        WebToken token = WebToken.ofJwt(jwt);
        return ok(
                Envelope.ok(token)
        );
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<Envelope<WebToken>> login(@RequestBody @Valid Login request) {

        return ok(
                Envelope.ok(authService.login(request))
        );
    }

//    @Override
//    @GetMapping("/oauth2/authorization/{provider}")
//    public ResponseEntity<Envelope<OAuth2Uri>> retrieveOAuth2Uri(@PathVariable String provider) {
//
//        return ResponseEntity.ok(
//                Envelope.ok(oAuth2Properties
//                        .findProviderWithName(provider)
//                        .makeOAuth2UriResponse()
//                ));
//    }


//    @Override
//    @PostMapping("/oauth2/code/{provider}")
//    public ResponseEntity<Envelope<Jwt>> handleOAuth2(@PathVariable String provider,
//                                                        @RequestParam String code) {
//
//        OAuth2Properties.Provider providerInfo = oAuth2Properties.findProviderWithName(provider);
//
//        String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);
//        Envelope<Jwt> oAuth2Response = authService.processOAuth2(providerInfo, decodedCode);
//
//        return ResponseEntity.ok(oAuth2Response);
//    }


    @Override
    @PostMapping("/auth/refresh")
    public ResponseEntity<Envelope<WebToken>> refreshAccessToken(@RequestBody @Valid WebToken request) {

        final String accessToken = request.getAccessToken();
        final String refreshToken = request.getRefreshToken();

        return ok(
                Envelope.ok(authService.refreshAccessToken(accessToken, refreshToken))
        );
    }


    @Override
    @GetMapping("/check")
    public ResponseEntity<Envelope<Long>> checkToken() {

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ok(
                Envelope.ok(Long.parseLong(principal.getUsername()))
        );
    }


    @Override
    @PostMapping("/sms/certification")
    public ResponseEntity<Envelope<SmsCertification>> sendJoinCertificationNumber(@RequestParam String phoneNumber) {

        return ok(
                Envelope.ok(authService.sendCertificationNumber(phoneNumber))
        );
    }


    @Override
    @PostMapping("/sms/verification")
    public ResponseEntity<Envelope<Void>> verifyJoinCertificationNumber(@RequestBody @Valid SmsVerification request) {

        final String phoneNumber = request.getPhoneNumber();
        final String certificationNumber = request.getCertificationNumber();

        authService.verifyCertificationNumber(phoneNumber, certificationNumber);
        return ok(
                Envelope.empty()
        );
    }


}
