package much.api.common.exception;

import static much.api.common.enums.Code.INVALID_DEADLINE;

public class InvalidDeadLine extends MuchException {

    public InvalidDeadLine() {
        super(INVALID_DEADLINE, "모집 마감일 누락");
    }

}
