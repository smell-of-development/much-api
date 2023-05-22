package much.api.service;

import much.api.common.properties.OAuth2Properties;
import much.api.dto.Jwt;
import much.api.dto.response.Envelope;
import much.api.dto.response.OAuth2Response;

public interface AuthService {

    Envelope<Jwt> refreshAccessToken(String accessToken, String refreshToken);

    Envelope<OAuth2Response> processOAuth2(OAuth2Properties.Provider providerInfo, String code);

}
