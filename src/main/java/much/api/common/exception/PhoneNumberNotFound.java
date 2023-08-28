package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.PHONE_NUMBER_NOT_FOUND;

public class PhoneNumberNotFound extends MuchException {

    public PhoneNumberNotFound(String phoneNumber) {
        super(PHONE_NUMBER_NOT_FOUND, format("휴대폰번호: [%s] - 미존재", phoneNumber));
    }

}
