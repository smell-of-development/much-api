package much.api.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.CERTIFICATION_MESSAGE_SENDING_NEEDED;

public class CertificationMessageSendingNeeded extends MuchException {

    public CertificationMessageSendingNeeded(String phoneNumber) {
        super(CERTIFICATION_MESSAGE_SENDING_NEEDED, format("입력 휴대폰번호: [%s] - 전송기록 미존재", phoneNumber));
    }

}
