package much.api.common.exception;

import static much.api.common.enums.Code.ALREADY_JOINED_PROJECT;

public class AlreadyJoinedProject extends MuchException {

    public AlreadyJoinedProject() {
        super(ALREADY_JOINED_PROJECT, ALREADY_JOINED_PROJECT.getMessage());
    }

}
