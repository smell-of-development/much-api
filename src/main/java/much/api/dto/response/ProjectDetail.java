package much.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import much.api.common.enums.MuchState;
import much.api.common.util.ContextUtils;
import much.api.entity.Project;
import much.api.entity.ProjectJoin;
import much.api.entity.ProjectPosition;
import much.api.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.stream;
import static much.api.common.enums.MuchState.DONE;
import static much.api.common.enums.MuchState.RECRUITING;
import static much.api.dto.response.ProjectDetail.Recruit.State.ofState;
import static much.api.dto.response.ProjectDetail.Writer.ofUser;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ProjectDetail {

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

    public static ProjectDetail ofEntity(Project project, Set<String> tags) {

        List<ProjectPosition> projectPositions = project.getPositionStatus();

        Long userId = ContextUtils.getUserId();
        boolean isLoggedOut = (userId == null);
        boolean isWriter = project.isWriter();

        Boolean alreadyJoined = isLoggedOut ? null
                : isWriter || isJoined(userId, project.getProjectJoins());
        Boolean alreadyApplied = isLoggedOut ? null
                : !alreadyJoined && hasApplied(userId, project.getApplications());

        return ProjectDetail.builder()
                .writer(ofUser(project.getWriter()))
                .editable(isWriter)
                .alreadyJoined(alreadyJoined)
                .alreadyApplied(alreadyApplied)
                .id(project.getId())
                .title(project.getTitle())
                .imageUrl(project.getImageUrl())
                .online(project.isOnline())
                .address(project.getAddress())
                .deadline(project.getDeadline())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .deadlineDDay(project.getDeadlineDDay())
                .between(project.getBetween())
                .meetingDays(
                        project.getMeetingDays() == null ?
                                null : stream(project.getMeetingDays().split(",")).toList()
                )
                .recruit(Recruit.of(projectPositions))
                .tags(tags)
                .introduction(project.getIntroduction())
                .build();
    }

    private static boolean hasApplied(Long userId, List<much.api.entity.ProjectApplication> applications) {
        if (userId == null) return false;

        return applications.stream()
                .anyMatch(ap -> ap.getMember().getId().equals(userId));
    }

    private static boolean isJoined(Long userId, List<ProjectJoin> projectJoins) {
        if (userId == null) return false;

        return projectJoins.stream()
                .anyMatch(pj -> pj.getMember().getId().equals(userId));
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

        // 각 포지션 현황
        private List<PositionStatus> positionStatus;


        private Recruit(State state,
                        List<PositionStatus> positionStatus,
                        Integer needs,
                        Integer recruited) {

            this.state = state;
            this.positionStatus = positionStatus;
            this.needs = needs;
            this.recruited = recruited;
        }


        public static Recruit of(List<ProjectPosition> projectPositions) {

            int totalNeeds = 0;
            int totalRecruited = 0;

            boolean findedContainsMe = false;

            int closedPositionCount = 0;

            List<PositionStatus> positionStatusList = new ArrayList<>();
            for (ProjectPosition position : projectPositions) {

                final int needs = position.getNeeds();
                final int recruited = position.getRecruited();

                totalNeeds += needs;
                totalRecruited += recruited;

                // 포지션 마감 확인
                if (position.isClosed()) {
                    closedPositionCount++;
                }

                // containsMe : 작성자 == 조회자 == 포지션 가입자
                boolean containsMe = false;
                // 전체중 한번 발견
                if (!findedContainsMe && position.isContainsMe()) {
                    findedContainsMe = true;
                    containsMe = true;
                }

                // deletable : 작성자 == 조회자 && 포지션 정보를 지울 수 있는지?
                boolean deletable = position.isDeletable();

                positionStatusList.add(
                        PositionStatus.builder()
                                .state(position.isClosed() ? ofState(DONE) : ofState(RECRUITING))
                                .id(position.getId())
                                .name(position.getName())
                                .needs(needs)
                                .recruited(recruited)
                                .deletable(deletable)
                                .containsMe(containsMe)
                                .build());
            }

            // 전체 포지션 마감 확인
            State recruitState = ofState(positionStatusList.size() == closedPositionCount ? DONE : RECRUITING);

            return new Recruit(recruitState, positionStatusList, totalNeeds, totalRecruited);
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

        @Getter
        @Builder
        static class PositionStatus {

            // 포지션 상태
            private State state;

            // 포지션 고유 ID (이름이 같아도 글마다 다름)
            private Long id;

            // 포지션 이름
            private String name;

            // 모집 인원
            private Integer needs;

            // 모집된 인원
            private Integer recruited;

            // 작성자 == 조회자 == 가입자
            private boolean containsMe;

            // 지울 수 있는가?
            private boolean deletable;


        }

    }


}
