package much.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import much.api.dto.request.SmsVerification;
import much.api.dto.response.SmsCertification;
import much.api.exception.InvalidValueException;
import much.api.common.properties.OAuth2Properties;
import much.api.controller.swagger.AuthApi;
import much.api.dto.Jwt;
import much.api.dto.response.Envelope;
import much.api.dto.response.OAuth2;
import much.api.dto.response.OAuth2Uri;
import much.api.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    private final OAuth2Properties oAuth2Properties;



    @Override
    @GetMapping("/oauth2/authorization/{provider}")
    public ResponseEntity<Envelope<OAuth2Uri>> retrieveOAuth2Uri(@PathVariable String provider) {

        return ResponseEntity.ok(
                Envelope.ok(oAuth2Properties
                        .findProviderWithName(provider)
                        .makeOAuth2UriResponse()
                ));
    }


    @Override
    @PostMapping("/oauth2/code/{provider}")
    public ResponseEntity<Envelope<OAuth2>> handleOAuth2(@PathVariable String provider,
                                                         @RequestParam String code) {

        OAuth2Properties.Provider providerInfo = oAuth2Properties.findProviderWithName(provider);

        String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);
        Envelope<OAuth2> oAuth2Response = authService.processOAuth2(providerInfo, decodedCode);

        return ResponseEntity.ok(oAuth2Response);
    }


    @Override
    @PostMapping("/auth/refresh")
    public ResponseEntity<Envelope<Jwt>> refreshAccessToken(@RequestBody Jwt request) {

        final String accessToken = request.getAccessToken();
        final String refreshToken = request.getRefreshToken();

        if (!StringUtils.hasText(accessToken)) {
            throw new InvalidValueException("accessToken");
        }
        if (!StringUtils.hasText(refreshToken)) {
            throw new InvalidValueException("refreshToken");
        }

        return ResponseEntity.ok(authService.refreshAccessToken(accessToken, refreshToken));
    }


    @Override
    @GetMapping("/check")
    public ResponseEntity<Envelope<Long>> checkToken() {

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(Envelope.ok(Long.parseLong(principal.getUsername())));
    }


    @Override
    @PostMapping("/sms/certification")
    public ResponseEntity<Envelope<SmsCertification>> sendCertificationNumber(@RequestParam String phoneNumber) {

        if (!StringUtils.hasText(phoneNumber)) {
            throw new InvalidValueException("phoneNumber");
        }

        Envelope<SmsCertification> response = authService.sendCertificationNumber(phoneNumber);
        return ResponseEntity.ok(response);
    }


    @Override
    @PostMapping("/sms/verification")
    public ResponseEntity<Envelope<Void>> verifyCertificationNumber(@RequestBody SmsVerification request) {

        final Long id = request.getId();
        final String phoneNumber = request.getPhoneNumber();
        final String certificationNumber = request.getCertificationNumber();

        if (id == null) {
            throw new InvalidValueException("id");
        }
        if (!StringUtils.hasText(phoneNumber)) {
            throw new InvalidValueException("phoneNumber");
        }
        if (!StringUtils.hasText(certificationNumber)) {
            throw new InvalidValueException("certificationNumber");
        }

        Envelope<Void> response = authService.verifyCertificationNumber(id, phoneNumber, certificationNumber);
        return ResponseEntity.ok(response);
    }
}
