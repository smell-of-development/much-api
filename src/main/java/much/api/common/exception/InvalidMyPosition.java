package much.api.common.exception;

import static much.api.common.enums.Code.INVALID_MY_POSITION;

public class InvalidMyPosition extends MuchException {

    public InvalidMyPosition() {
        super(INVALID_MY_POSITION, "나의 포지션 선택 이상");
    }

}
