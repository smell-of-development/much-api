package much.api.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.CERTIFICATION_NUMBER_NOT_MATCHED;

public class CertificationNumberNotMatched extends MuchException {

    public CertificationNumberNotMatched(String number) {
        super(CERTIFICATION_NUMBER_NOT_MATCHED, format("입력 NUMBER: [%s] - 일치하지 않음", number));
    }

}
