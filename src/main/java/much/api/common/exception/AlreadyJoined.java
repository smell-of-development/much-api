package much.api.common.exception;

import static much.api.common.enums.Code.ALREADY_JOINED;

public class AlreadyJoined extends MuchException {

    public AlreadyJoined() {
        super(ALREADY_JOINED, ALREADY_JOINED.getMessage());
    }

}
