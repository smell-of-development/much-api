package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.INVALID_DEADLINE;

public class InvalidDeadLine extends MuchException {

    public InvalidDeadLine() {
        super(INVALID_DEADLINE, "올바르지 않은 마감일");
    }

    public InvalidDeadLine(String date) {
        super(INVALID_DEADLINE, format("올바르지 않은 마감일: [%s]", date));
    }

}
