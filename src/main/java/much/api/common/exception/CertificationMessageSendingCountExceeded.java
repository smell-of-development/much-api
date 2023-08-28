package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.CERTIFICATION_MESSAGE_SENDING_COUNT_EXCEEDED;

public class CertificationMessageSendingCountExceeded extends MuchException {

    public CertificationMessageSendingCountExceeded(String phoneNumber) {
        super(CERTIFICATION_MESSAGE_SENDING_COUNT_EXCEEDED, format("입력 휴대폰번: [%s] - 일일 전송건수 초과", phoneNumber));
    }

}
