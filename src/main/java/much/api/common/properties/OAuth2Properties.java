package much.api.common.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import much.api.common.enums.Code;
import much.api.common.enums.OAuth2Provider;
import much.api.exception.NotSupportedException;
import much.api.dto.response.OAuth2Uri;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth2")
public class OAuth2Properties {

    public static final String RESPONSE_TYPE_KEY = "response_type";
    public static final String CLIENT_ID_KEY = "client_id";
    public static final String SCOPE_KEY = "scope";
    public static final String REDIRECT_URI_KEY = "redirect_uri";
    public static final String GRANT_TYPE_KEY = "grant_type";
    public static final String CODE_KEY = "code";
    public static final String CLIENT_SECRET_KEY = "client_secret";

    private final List<Provider> providers;

    public Provider findProviderWithName(String name) {

        return getProviders().stream()
                .filter(p -> name.equalsIgnoreCase(p.getName()))
                .findFirst()
                .orElseThrow(() -> new NotSupportedException(Code.NOT_SUPPORTED_OAUTH2_PROVIDER, String.format("지원하지 않는 프로바이더[%s]", name)));
    }

    @Getter
    @RequiredArgsConstructor
    public static class Provider {

        private final String name;

        private final String clientId;

        private final String clientSecret;

        private final String responseType;

        private final String authorizationGrantType;

        private final String authorizationUri;

        private final String redirectUri;

        private final String tokenUri;

        private final String userInfoUri;

        private final String scope;

        public String makeUserSocialId(String socialId) {

            return getEnum().name() + "_" + socialId;
        }

        public OAuth2Provider getEnum() {

            return OAuth2Provider.valueOf(getName().toUpperCase());
        }

        public OAuth2Uri makeOAuth2UriResponse() {

            return new OAuth2Uri(this.getName(), getLoginUri());
        }

        public String getLoginUri() {

            return fromHttpUrl(this.getAuthorizationUri())
                    .queryParam(RESPONSE_TYPE_KEY, this.getResponseType())
                    .queryParam(CLIENT_ID_KEY, this.getClientId())
                    .queryParam(SCOPE_KEY, this.getScope())
                    .queryParam(REDIRECT_URI_KEY, this.getRedirectUri())
                    .toUriString();
        }
    }


}
