package much.api.common.exception;

import static much.api.common.enums.Code.ALREADY_APPLIED_PROJECT;

public class AlreadyAppliedProject extends MuchException {

    public AlreadyAppliedProject() {
        super(ALREADY_APPLIED_PROJECT, ALREADY_APPLIED_PROJECT.getMessage());
    }

}
