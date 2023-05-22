package much.api.exception;

import much.api.common.enums.Code;

public class RequiredException extends BusinessException {

    public RequiredException(String name) {
        super(String.format("필수정보[%s]없음", name));
        this.code = Code.REQUIRED_INFORMATION;
    }

}
