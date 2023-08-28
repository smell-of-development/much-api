package much.api.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.CERTIFICATION_NUMBER_SENDING_NEEDED;

public class CertificationNumberSendingNeeded extends MuchException {

    public CertificationNumberSendingNeeded(String phoneNumber) {
        super(CERTIFICATION_NUMBER_SENDING_NEEDED, format("입력 휴대폰번호: [%s] - 인증번호 전송 필요", phoneNumber));
    }

}
