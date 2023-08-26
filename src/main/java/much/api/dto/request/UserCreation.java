package much.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import much.api.dto.Check;

@Getter
public class UserCreation {

    @NotNull
    @Check("isValidLoginId")
    private String loginId;

    @NotNull
    @Check("isValidPassword")
    private String password;

    @NotNull
    @Check("isValidNickname")
    private String nickname;

    @NotNull
    @Check("isValidPhoneNumber")
    private String phoneNumber;

    private String position;

    public UserCreation() {
    }

    @Builder
    public UserCreation(@NotNull String loginId,
                        @NotNull String password,
                        @NotNull String nickname,
                        @NotNull String phoneNumber,
                        String position) {

        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.position = position;
    }
}
