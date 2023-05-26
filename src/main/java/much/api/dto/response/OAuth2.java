package much.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OAuth2 {

    private final Long id;

    private final String accessToken;

    private final String refreshToken;

}
