package much.api.common.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private final String header;

    private final String type;

    private final String secret;

    private final long accessTokenExpirationTime;

    private final long refreshTokenExpirationTime;

}
