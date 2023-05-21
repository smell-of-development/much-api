package much.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import much.api.common.properties.OAuth2Properties;
import much.api.controller.swagger.AuthApi;
import much.api.dto.Jwt;
import much.api.dto.response.Envelope;
import much.api.dto.response.OAuth2Response;
import much.api.dto.response.OAuth2UriResponse;
import much.api.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    private final OAuth2Properties oAuth2Properties;


    @Override
    @GetMapping("/oauth2/authorization/{provider}")
    public ResponseEntity<Envelope<OAuth2UriResponse>> retrieveOAuth2Uri(@PathVariable String provider) {

        final String toLowerCase = provider.toLowerCase();

        return ResponseEntity.ok(
                Envelope.ok(oAuth2Properties
                        .findProviderWithName(toLowerCase)
                        .makeOAuth2UriResponse()
                ));
    }


    @Override
    @GetMapping("/oauth2/code/{provider}")
    public ResponseEntity<Envelope<OAuth2Response>> handleOAuth2(@PathVariable String provider,
                                                                 @RequestParam String code) {

        final String toLowerCase = provider.toLowerCase();
        OAuth2Properties.Provider providerInfo = oAuth2Properties.findProviderWithName(toLowerCase);

        Envelope<OAuth2Response> oAuth2Response = authService.processOAuth2(providerInfo, code);

        return ResponseEntity.ok(oAuth2Response);
    }


    @Override
    @PostMapping("/auth/refresh")
    public ResponseEntity<Envelope<Jwt>> refreshAccessToken(@RequestBody @Valid Jwt jwt) {

        // TODO 구현하기
        return ResponseEntity.ok(Envelope.ok(new Jwt(jwt.getAccessToken())));
    }


}