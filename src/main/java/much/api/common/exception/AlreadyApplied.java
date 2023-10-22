package much.api.common.exception;

import static much.api.common.enums.Code.ALREADY_APPLIED;

public class AlreadyApplied extends MuchException {

    public AlreadyApplied() {
        super(ALREADY_APPLIED, ALREADY_APPLIED.getMessage());
    }

}
