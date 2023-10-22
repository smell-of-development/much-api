package much.api.common.exception;

import static much.api.common.enums.Code.INVALID_MEETING_TYPE;

public class InvalidMeetingType extends MuchException {

    public InvalidMeetingType() {
        super(INVALID_MEETING_TYPE, "모임방식 누락");
    }

}
