package much.api.common.exception;

import static java.lang.String.*;
import static much.api.common.enums.Code.INVALID_RECRUIT_REQUIRED_PEOPLE;

public class InvalidRecruitRequiredPeople extends MuchException {

    public InvalidRecruitRequiredPeople(String target, Integer input) {
        super(INVALID_RECRUIT_REQUIRED_PEOPLE, format("포지션 인원수 이상 [%s: %s]", target, input));
    }

}
