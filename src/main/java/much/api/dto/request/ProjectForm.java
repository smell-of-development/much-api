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
public class ProjectForm {

    private static final int MAX_TITLE_LENGTH = 40;
    private static final int MIN_TITLE_LENGTH = 1;

    private static final int MAX_NAME_LENGTH = 20;
    private static final int MIN_NAME_LENGTH = 1;

    private String title;

    private String imageUrl;

    private Boolean online;

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

    private Recruit recruit;

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

        if (recruit == null) {
            throw new InvalidRecruit();
        }

        // 포지션 모집 인원은 1 이상
        // 본인이 포함된 포지션이 하나 체크되었는지 확인
        boolean containsMyPosition = false;
        for (Recruit.PositionStatus p : recruit.getPositionStatus()) {
            String name = p.getName();
            int needs = p.getNeeds();

            int nameLength = StringUtils.length(name);
            if (nameLength < MIN_NAME_LENGTH || nameLength > MAX_NAME_LENGTH) {
                throw new InvalidLength("포지션 이름은", MIN_NAME_LENGTH, MAX_NAME_LENGTH, nameLength);
            }

            if (needs < 1) {
                throw new InvalidRecruitRequiredPeople(name, needs);
            }

            // 본인 포함 포지션 선택이 2번 이상 X
            if (containsMyPosition && p.isContainsMe()) {
                throw new InvalidMyPosition();
            }

            if (containsMyPosition) {
                continue;
            }
            containsMyPosition = p.isContainsMe();
        }
        // 본인 포함 포지션 선택이 없으면
        if (!containsMyPosition) {
            throw new InvalidMyPosition();
        }

        // TODO 소개 내용 글자수 제한?
    }


    @Getter
    public static class Recruit {

        List<PositionStatus> positionStatus = new ArrayList<>();

        @Getter
        public static class PositionStatus {

            private Long id;

            private String name;

            private int needs;

            private boolean containsMe;

        }

    }

}
