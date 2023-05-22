package much.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Jwt {

    private final String accessToken;

    private String refreshToken;

    public Jwt(String accessToken) {
        this.accessToken = accessToken;
    }

}
