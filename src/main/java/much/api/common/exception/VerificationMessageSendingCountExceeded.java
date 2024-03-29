package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.VERIFICATION_MESSAGE_SENDING_COUNT_EXCEEDED;

public class VerificationMessageSendingCountExceeded extends MuchException {

    public VerificationMessageSendingCountExceeded(String phoneNumber) {
        super(VERIFICATION_MESSAGE_SENDING_COUNT_EXCEEDED, format("입력 휴대폰 번호: [%s] - 일일 전송수 초과", phoneNumber));
    }

}
