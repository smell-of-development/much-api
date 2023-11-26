package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.VERIFICATION_NEEDED;

public class VerificationNeeded extends MuchException {

    public VerificationNeeded(String phoneNumber) {
        super(VERIFICATION_NEEDED, format("입력 휴대폰번호: [%s] - 인증필요", phoneNumber));
    }

}
