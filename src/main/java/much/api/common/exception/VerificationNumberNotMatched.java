package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.VERIFICATION_NUMBER_NOT_MATCHED;

public class VerificationNumberNotMatched extends MuchException {

    public VerificationNumberNotMatched(String number) {
        super(VERIFICATION_NUMBER_NOT_MATCHED, format("입력 NUMBER: [%s] - 일치하지 않음", number));
    }

}
