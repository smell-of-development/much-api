package much.api.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.DUPLICATED_LOGIN_ID;

public class DuplicatedLoginID extends MuchException {

    public DuplicatedLoginID(Long id) {
        super(DUPLICATED_LOGIN_ID, format("입력 ID: [%s] - 중복됨", id));
    }

}
