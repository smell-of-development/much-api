package much.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class Login {

    @NotNull
    private String loginId;

    @NotNull
    private String password;

}
