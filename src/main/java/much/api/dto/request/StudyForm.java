package much.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import much.api.common.aop.SelfCheck;
import much.api.common.exception.*;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@SelfCheck("checkValidation")
public class StudyForm {

    private static final int MAX_TITLE_LENGTH = 40;
    private static final int MIN_TITLE_LENGTH = 1;

    private String title;

    private String imageUrl;

    protected Boolean online;

    private String address;

    @JsonFormat(pattern = "yyyy.MM.dd")
    @Schema(example = "yyyy.MM.dd", type = "string")
    private LocalDate deadline;

    @JsonFormat(pattern = "yyyy.MM.dd")
    @Schema(example = "yyyy.MM.dd", type = "string")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy.MM.dd")
    @Schema(example = "yyyy.MM.dd", type = "string")
    private LocalDate endDate;

    private List<String> meetingDays = new ArrayList<>();

    private int needs;

    private Set<String> tags = new HashSet<>();

    private String introduction;


    private void checkValidation() {

        int titleLength = StringUtils.length(title);
        if (titleLength < MIN_TITLE_LENGTH || titleLength > MAX_TITLE_LENGTH) {
            throw new InvalidLength("제목은", MIN_TITLE_LENGTH, MAX_TITLE_LENGTH, titleLength);
        }

        if (online == null) {
            throw new InvalidMeetingType();
        }

        if (deadline == null) {
            throw new InvalidDeadLine();
        }

        // 일정 협의
        if (startDate == null && endDate == null) {
            return;
        }

        if (startDate == null || endDate == null) {
            throw new InvalidPeriod();
        }

        // 일정 시작 ~ 종료일 확인
        if (startDate.isAfter(endDate)) {
            throw new InvalidPeriod(startDate.toString(), endDate.toString());
        }

        if (needs < 1) {
            throw new InvalidRequiredPeople(needs);
        }
    }

}
