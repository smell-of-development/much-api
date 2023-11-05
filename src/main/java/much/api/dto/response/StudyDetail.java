package much.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import much.api.common.enums.MuchState;
import much.api.common.util.ContextUtils;
import much.api.entity.Study;
import much.api.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.stream;
import static much.api.common.enums.MuchState.*;
import static much.api.dto.response.StudyDetail.Recruit.State.*;
import static much.api.dto.response.StudyDetail.Writer.ofUser;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class StudyDetail {

    // 작성자 정보
    private Writer writer;

    // 수정 가능 여부
    private Boolean editable;

    // 가입 되어있는지
    private Boolean alreadyJoined;

    // 신청 했는지 여부
    private Boolean alreadyApplied;

    // 게시글 고유 ID
    private Long id;

    // 제목
    private String title;

    // 이미지 주소
    private String imageUrl;

    // 온라인 모임?
    private boolean online;

    // 모임 위치 협의("")
    private String address;

    // 모집 마감일
    @JsonFormat(pattern = "yyyy.MM.dd")
    @Schema(example = "yyyy.MM.dd", type = "string")
    private LocalDate deadline;

    // 마감일 D-Day
    private long deadlineDDay;

    // 모임 시작일
    @JsonFormat(pattern = "yyyy.MM.dd")
    @Schema(example = "yyyy.MM.dd", type = "string")
    private LocalDate startDate;

    // 모임 종료일
    @JsonFormat(pattern = "yyyy.MM.dd")
    @Schema(example = "yyyy.MM.dd", type = "string")
    private LocalDate endDate;

    // 모임 종료일 - 모임 마감일 차이 또는 협의("")
    private Long between;

    // 모임일 ex) "월", "수", "금"
    private List<String> meetingDays;

    // 모집 정보
    private Recruit recruit;

    // 태그
    private Set<String> tags;

    // 소개 내용
    private String introduction;


    public static StudyDetail ofEntity(Study study, Set<String> tags) {

        study.increaseViewCount();

        Long userId = ContextUtils.getUserId();
        boolean isLoggedOut = (userId == null);
        boolean isWriter = study.isWriter();

        Boolean alreadyJoined = isLoggedOut ? null
                : isWriter || study.hasJoinedUser(userId);
        Boolean alreadyApplied = isLoggedOut ? null
                : !alreadyJoined && study.hasAppliedUser(userId);

        return StudyDetail.builder()
                .writer(ofUser(study.getWriter()))
                .editable(isWriter)
                .alreadyJoined(alreadyJoined)
                .alreadyApplied(alreadyApplied)
                .id(study.getId())
                .title(study.getTitle())
                .imageUrl(study.getImageUrl())
                .online(study.isOnline())
                .address(study.getAddress())
                .deadline(study.getDeadline())
                .startDate(study.getStartDate())
                .endDate(study.getEndDate())
                .deadlineDDay(study.getDeadlineDDay())
                .between(study.getBetween())
                .meetingDays(
                        study.getMeetingDays() == null ?
                                null : stream(study.getMeetingDays().split(",")).toList()
                )
                .recruit(Recruit.ofEntity(study))
                .tags(tags)
                .introduction(study.getIntroduction())
                .build();
    }


    @Getter
    static class Writer {

        // 작성자 고유 ID
        private Long id;

        // 작성자 닉네임
        private String nickname;

        // 작성자 이미지 주소
        private String imageUrl;


        private Writer(Long id, String nickname, String imageUrl) {
            this.id = id;
            this.nickname = nickname;
            this.imageUrl = imageUrl;
        }


        public static Writer ofUser(User writer) {

            return new Writer(writer.getId(), writer.getNickname(), writer.getImageUrl());
        }
    }


    @Getter
    public static class Recruit {

        // 전체 모집 상태
        private State state;

        // 전체 모집 인원
        private Integer needs;

        // 전체 모집된 인원
        private Integer recruited;


        private Recruit(State state, Integer needs, Integer recruited) {
            this.state = state;
            this.needs = needs;
            this.recruited = recruited;
        }


        public static Recruit of(boolean closed, Integer needs, Integer recruited) {

            return new Recruit(ofState(closed ? DONE : RECRUITING), needs, recruited);
        }


        public static Recruit ofEntity(Study study) {

            return new Recruit(ofState(study.isClosed() ? DONE : RECRUITING), study.getNeeds(), study.getRecruited());
        }

        @Getter
        static class State {

            // DONE 또는 RECRUITING
            private String code;

            // 의미(한글)
            private String name;

            private State(String code, String name) {
                this.code = code;
                this.name = name;
            }

            public static State ofState(MuchState state) {

                return new State(state.name(), state.getMeaning());
            }
        }
    }

}
