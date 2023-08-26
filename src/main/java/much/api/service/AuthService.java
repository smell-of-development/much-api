package much.api.service;

import much.api.common.properties.OAuth2Properties;
import much.api.dto.response.WebToken;
import much.api.dto.request.Login;
import much.api.dto.response.Envelope;
import much.api.dto.response.SmsCertification;

public interface AuthService {

    Envelope<WebToken> login(Login loginRequest);

    Envelope<WebToken> refreshAccessToken(String accessToken, String refreshToken);

    Envelope<WebToken> processOAuth2(OAuth2Properties.Provider providerInfo, String code);

    Envelope<SmsCertification> sendCertificationNumber(String phoneNumber);

    void verifyCertificationNumber(String phoneNumber, String certificationNumber);

}
