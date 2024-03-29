package much.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import much.api.common.aop.Check;

@Getter
public class UserCreation {

    @NotNull
    @Check("checkLoginId")
    private String loginId;

    @NotNull
    @Check("checkPassword")
    private String password;

    @NotNull
    @Check("checkNickname")
    private String nickname;

    @NotNull
    @Check("checkPhoneNumber")
    private String phoneNumber;

    private String position;

    public UserCreation() {
    }

    @Builder
    public UserCreation(String loginId,
                        String password,
                        String nickname,
                        String phoneNumber,
                        String position) {

        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.position = position;
    }
}
