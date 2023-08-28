package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.CERTIFICATION_NEEDED;

public class CertificationNeeded extends MuchException {

    public CertificationNeeded(String phoneNumber) {
        super(CERTIFICATION_NEEDED, format("입력 휴대폰번호: [%s] - 인증필요", phoneNumber));
    }

}
