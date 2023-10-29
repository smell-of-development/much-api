package much.api.dto.request;

import lombok.Getter;
import much.api.common.aop.SelfCheck;
import much.api.common.exception.InvalidLength;

@Getter
@SelfCheck("checkValidation")
public class StudyApplicationForm {

    private String memo;

    private void checkValidation() {

        if (memo != null && memo.length() > 100) {
            throw new InvalidLength("신청메모는", 0, 100, memo.length());
        }
    }

}
