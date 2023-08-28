package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.INVALID_NICKNAME;

public class InvalidNickname extends MuchException {

    public InvalidNickname(String nickname) {
        super(INVALID_NICKNAME, format("입력 닉네임: [%s] - 형식 잘못됨", nickname));
    }

}
