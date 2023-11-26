package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.VERIFICATION_NUMBER_SENDING_NEEDED;

public class VerificationNumberSendingNeeded extends MuchException {

    public VerificationNumberSendingNeeded(String phoneNumber) {
        super(VERIFICATION_NUMBER_SENDING_NEEDED, format("입력 휴대폰번호: [%s] - 인증번호 전송 필요", phoneNumber));
    }

}
