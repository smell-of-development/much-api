package much.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class Login {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;

}
