package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.INCORRECT_LOGIN_INFO;

public class IncorrectLoginInfo extends MuchException {

    public IncorrectLoginInfo(String loginId) {
        super(INCORRECT_LOGIN_INFO, format("사용자 ID: [%s] - ID 또는 PW 불일치", loginId));
    }

}
