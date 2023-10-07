package much.api.common.exception;

import static java.lang.String.format;
import static much.api.common.enums.Code.POSITION_CAN_NOT_BE_DELETED;

public class PositionCanNotBeDeleted extends MuchException {

    public PositionCanNotBeDeleted(String position) {
        super(POSITION_CAN_NOT_BE_DELETED, format(POSITION_CAN_NOT_BE_DELETED.getMessage(), position)
        );
    }

}
