package much.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Jwt {

    @NotBlank(message = "필수 값입니다.")
    private final String accessToken;

    @NotBlank(message = "필수 값입니다.")
    private String refreshToken;

    public Jwt(String accessToken) {
        this.accessToken = accessToken;
    }

    protected Jwt(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
