package much.api.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import much.api.common.aop.SelfCheck;
import much.api.common.exception.InvalidDeadLine;
import much.api.common.exception.InvalidLength;
import much.api.common.exception.InvalidPeriod;
import much.api.common.exception.InvalidRecruitNeeds;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@SelfCheck("checkValidation")
public class ProjectCreation {

    @NotNull
    private String title;

    private String imageUrl;

    @NotNull
    private Boolean online;

    private String address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private List<String> timesPerWeek = new ArrayList<>();

    @NotEmpty
    private List<Position> recruit;

    private Set<String> tags = new HashSet<>();

    private String introduction;

    @Getter
    public static class Position {

        private String name;

        private Integer needs;

    }


    private void checkValidation() {

        // 제목 1~40자 이하
        if (title.length() > 40) {
            throw new InvalidLength("제목은", 40, title.length());
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
        recruit.forEach(p -> {
                    String name = p.name;
                    Integer needs = p.needs;

                    if (needs < 1) {
                        throw new InvalidRecruitNeeds(name, needs);
                    }
                });

        // TODO 사용자 입력 내용 글자수 정해서 제한
    }


}
