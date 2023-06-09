package much.api.service;

import much.api.common.properties.OAuth2Properties;
import much.api.dto.Jwt;
import much.api.dto.request.Login;
import much.api.dto.response.Envelope;
import much.api.dto.response.SmsCertification;

public interface AuthService {

    Envelope<Jwt> login(Login loginRequest);

    Envelope<Jwt> refreshAccessToken(String accessToken, String refreshToken);

    Envelope<Jwt> processOAuth2(OAuth2Properties.Provider providerInfo, String code);

    Envelope<SmsCertification> sendCertificationNumber(String phoneNumber);

    void verifyCertificationNumber(String phoneNumber, String certificationNumber);

}
