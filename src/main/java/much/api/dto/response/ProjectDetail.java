package much.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import much.api.common.enums.MuchState;
import much.api.entity.Project;
import much.api.entity.ProjectPosition;
import much.api.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static much.api.common.enums.MuchState.DONE;
import static much.api.common.enums.MuchState.RECRUITING;
import static much.api.dto.response.ProjectDetail.Recruit.State.ofState;
import static much.api.dto.response.ProjectDetail.WriterDetail.ofWriter;

@Getter
@Builder
public class ProjectDetail {

    private WriterDetail writer;

    private boolean editable;

    private Long id;

    private String title;

    private String imageUrl;

    private boolean isOnline;

    private String address;

    private String schedule;

    private String timesPerWeek;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    private long deadlineDDay;

    private Recruit recruit;

    private Set<String> tags;

    private String introduction;

    public static ProjectDetail ofEntity(Project project, Set<String> tags) {

        List<ProjectPosition> projectPositions = project.getPositionStatus();

        return ProjectDetail.builder()
                .writer(ofWriter(project.getWriter()))
                .editable(project.isWriter())
                .id(project.getId())
                .title(project.getTitle())
                .imageUrl(project.getImageUrl())
                .isOnline(project.isOnline())
                .address(project.getAddress())
                .schedule(project.getSchedule())
                .timesPerWeek(project.getTimesPerWeek())
                .deadline(project.getDeadline())
                .deadlineDDay(project.getDeadlineDDay())
                .recruit(Recruit.of(projectPositions))
                .tags(tags)
                .introduction(project.getIntroduction())
                .build();
    }


    @Getter
    public static class WriterDetail {

        private Long id;

        private String nickname;

        private String imageUrl;


        private WriterDetail(Long id, String nickname, String imageUrl) {
            this.id = id;
            this.nickname = nickname;
            this.imageUrl = imageUrl;
        }


        public static WriterDetail ofWriter(User writer) {

            return new WriterDetail(writer.getId(), writer.getNickname(), writer.getImageUrl());
        }
    }


    @Getter
    public static class Recruit {

        private State state;

        private Integer needs;

        private Integer recruited;

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


        public static Recruit of(List<ProjectPosition> positionStatus) {

            int totalNeeds = 0;
            int totalRecruited = 0;
            List<PositionStatus> positionStatusList = new ArrayList<>();

            int closedPositionCount = 0;
            for (ProjectPosition position : positionStatus) {

                int needs = position.getNeeds();
                int recruited = position.getRecruited();

                // 전체 포지션 카운팅
                totalNeeds += needs;
                totalRecruited += recruited;

                // 모집 완료 포지션 카운팅
                if (position.isClosed()) {
                    closedPositionCount++;
                }

                positionStatusList.add(
                        PositionStatus.builder()
                                .state(position.isClosed() ?
                                        ofState(DONE) : ofState(RECRUITING))
                                .name(position.getName())
                                .needs(needs)
                                .recruited(recruited)
                                .build());
            }

            boolean fullClosed = positionStatusList.size() == closedPositionCount;

            State recruitState = ofState(fullClosed ? DONE : RECRUITING);
            return new Recruit(recruitState, positionStatusList, totalNeeds, totalRecruited);
        }

        @Getter
        public static class State {

            private String code;

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
        public static class PositionStatus {

            private State state;

            private String name;

            private Integer needs;

            private Integer recruited;

        }

    }


}
