package much.api.service;

import much.api.common.properties.OAuth2Properties;
import much.api.dto.Jwt;
import much.api.dto.response.Envelope;
import much.api.dto.response.OAuth2;
import much.api.dto.response.SmsCertification;

public interface AuthService {

    Envelope<Jwt> refreshAccessToken(String accessToken, String refreshToken);

    Envelope<OAuth2> processOAuth2(OAuth2Properties.Provider providerInfo, String code);

    Envelope<SmsCertification> sendCertificationNumber(String phoneNumber);

    Envelope<Void> verifyCertificationNumber(Long id, String phoneNumber, String certificationNumber);

}
