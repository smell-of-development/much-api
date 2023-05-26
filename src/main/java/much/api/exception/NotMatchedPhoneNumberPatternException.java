package much.api.exception;

import much.api.common.enums.Code;

public class NotMatchedPhoneNumberPatternException extends BusinessException {

    public NotMatchedPhoneNumberPatternException(String number) {
        super(String.format("휴대폰번호 형식이 아님. [%s]", number));
        this.code = Code.NOT_MATCHED_PHONE_NUMBER_PATTERN;
    }
}
