package much.api.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.INVALID_PHONE_NUMBER;

public class InvalidPhoneNumber extends MuchException {

    public InvalidPhoneNumber(String phoneNumber) {
        super(INVALID_PHONE_NUMBER, format("입력 휴대폰번호: [%s] - 형식불일치", phoneNumber));
    }

}
