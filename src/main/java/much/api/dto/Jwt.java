package much.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Jwt {

    private String accessToken;

    private String refreshToken;

    public Jwt(String accessToken) {
        this.accessToken = accessToken;
    }

}
