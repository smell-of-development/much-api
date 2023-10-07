package much.api.common.exception;

import static java.lang.String.*;
import static much.api.common.enums.Code.INVALID_RECRUIT_NEEDS;

public class InvalidRecruitNeeds extends MuchException {

    public InvalidRecruitNeeds(String target, Integer input) {
        super(INVALID_RECRUIT_NEEDS, format("포지션 인원수 이상 [%s: %s]", target, input));
    }

}
