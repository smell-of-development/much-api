package much.api.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.DUPLICATED_PHONE_NUMBER;

public class DuplicatedPhoneNumber extends MuchException {

    public DuplicatedPhoneNumber(String phoneNumber) {
        super(DUPLICATED_PHONE_NUMBER, format("입력 휴대폰번호: [%s] - 중복됨", phoneNumber));
    }

}
