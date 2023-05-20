package much.api.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuth2UriResponse {

    private final String provider;

    private final String loginUri;

}
