package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.DUPLICATED_LOGIN_ID;

public class DuplicatedLoginID extends MuchException {

    public DuplicatedLoginID(String loginId) {
        super(DUPLICATED_LOGIN_ID, format("입력 ID: [%s] - 중복됨", loginId));
    }

}
