package much.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import much.api.common.util.TokenProvider;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebToken {

    @NotNull
    private Long id;

    @NotNull
    private String accessToken;

    @NotNull
    private String refreshToken;


    @Builder(access = AccessLevel.PRIVATE)
    private WebToken(Long id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


    public static WebToken ofJwt(TokenProvider.Jwt jwt) {

        return WebToken.builder()
                .id(jwt.getId())
                .accessToken(jwt.getAccessToken())
                .refreshToken(jwt.getRefreshToken())
                .build();
    }


}
