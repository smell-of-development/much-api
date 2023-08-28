package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.INCORRECT_LOGIN_INFO;

public class IncorrectLoginInfo extends MuchException {

    public IncorrectLoginInfo(Long id) {
        super(INCORRECT_LOGIN_INFO, format("사용자 PK: [%s] - ID 또는 PW 불일치", id));
    }

}
