package much.api.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.INVALID_LOGIN_ID;

public class InvalidLoginID extends MuchException {

    public InvalidLoginID(Long id) {
        super(INVALID_LOGIN_ID, format("입력 ID: [%s] - 형식 잘못됨", id));
    }

}
