package much.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import much.api.common.aop.SelfCheck;
import much.api.common.exception.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@SelfCheck("checkValidation")
public class ProjectModification {

    @NotNull
    private String title;

    private String imageUrl;

    @NotNull
    private Boolean online;

    private String address;

    @DateTimeFormat(pattern = "yyyy.MM.dd")
    @Schema(example = "yyyy.MM.dd", type = "string")
    private LocalDate deadline;

    @DateTimeFormat(pattern = "yyyy.MM.dd")
    @Schema(example = "yyyy.MM.dd", type = "string")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy.MM.dd")
    @Schema(example = "yyyy.MM.dd", type = "string")
    private LocalDate endDate;

    private List<String> meetingDays = new ArrayList<>();

    @NotNull
    private Recruit recruit;

    private Set<String> tags = new HashSet<>();

    private String introduction;


    private void checkValidation() {

        if (title.length() > 40) {
            throw new InvalidLength("제목은", 1, 40, title.length());
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

        // 포지션 모집 인원은 1 이상
        // 본인이 포함된 포지션이 하나 체크되었는지 확인
        boolean containsMyPosition = false;
        for (Recruit.PositionStatus p : recruit.getPositionStatus()) {

            String name = p.getName();
            int needs = p.getNeeds();

            int nameLength = StringUtils.length(name);
            if (nameLength == 0 || nameLength > 20) {
                throw new InvalidLength("포지션 이름은", 1, 20, nameLength);
            }

            if (needs < 1) {
                throw new InvalidRecruitNeeds(name, needs);
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

        // TODO 사용자 입력 내용 글자수 정해서 제한
    }


    @Getter
    public static class Recruit {

        List<PositionStatus> positionStatus;

        @Getter
        public static class PositionStatus {

            private Long id;

            private String name;

            private int needs;

            private boolean containsMe;

        }

    }

}
