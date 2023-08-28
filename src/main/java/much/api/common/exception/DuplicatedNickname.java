package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.DUPLICATED_NICKNAME;

public class DuplicatedNickname extends MuchException {

    public DuplicatedNickname(String nickname) {
        super(DUPLICATED_NICKNAME, format("입력 닉네임: [%s] - 중복됨", nickname));
    }

}
