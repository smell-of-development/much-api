package much.api.common.exception;

import static java.lang.String.*;
import static much.api.common.enums.Code.INVALID_PERIOD;

public class InvalidPeriod extends MuchException {

    public InvalidPeriod() {
        super(INVALID_PERIOD, "올바르지 않은 기간");
    }

    public InvalidPeriod(String start, String end) {
        super(INVALID_PERIOD, format("올바르지 않은 기간: [%s] ~ [%s]", start, end));
    }

}
