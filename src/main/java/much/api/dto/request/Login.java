package much.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Login {

    @NotNull
    private String loginId;

    @NotNull
    private String password;

    @Builder
    private Login(String loginId,
                  String password) {

        this.loginId = loginId;
        this.password = password;
    }
}
