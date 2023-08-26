package much.api.exception;

import static much.api.common.enums.Code.INVALID_PASSWORD;

public class InvalidPassword extends MuchException {

    public InvalidPassword() {
        super(INVALID_PASSWORD, "패스워드 형식이 잘못됨");
    }

}
