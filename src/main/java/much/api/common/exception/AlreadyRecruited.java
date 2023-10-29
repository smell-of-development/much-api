package much.api.common.exception;

import static much.api.common.enums.Code.ALREADY_RECRUITED;

public class AlreadyRecruited extends MuchException {

    public AlreadyRecruited() {
        super(ALREADY_RECRUITED, ALREADY_RECRUITED.getMessage());
    }

}
