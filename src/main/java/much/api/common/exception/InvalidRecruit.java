package much.api.common.exception;

import static much.api.common.enums.Code.INVALID_RECRUIT;

public class InvalidRecruit extends MuchException {

    public InvalidRecruit() {
        super(INVALID_RECRUIT, "모집 대상 포지션 누락");
    }

}
