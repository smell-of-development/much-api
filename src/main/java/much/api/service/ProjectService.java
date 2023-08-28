package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.Code;
import much.api.common.enums.MuchType;
import much.api.common.util.ContextUtils;
import much.api.dto.request.ProjectCreation;
import much.api.dto.response.ProjectDetail;
import much.api.entity.Project;
import much.api.entity.User;
import much.api.common.exception.MuchException;
import much.api.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final CommonService commonService;

    private final ProjectRepository projectRepository;


    @Transactional
    public Long createProject(ProjectCreation registration, MuchType type) {

        final String skills = String.join(",", registration.getSkills());
        final String introduction = registration.getIntroduction();


        long userId = ContextUtils.getUserId();
        User user = commonService.getUserOrThrowException(userId);

        Project project = Project.builder()
                .writer(user)
                .type(type)
                .title(registration.getTitle())
                .imageUrl(registration.getImageUrl())
                .isOnline(registration.isOnline())
                .location(registration.getLocation())
                .deadline(registration.getDeadline())
                .startDate(registration.getStartDate())
                .endDate(registration.getEndDate())
                .schedule(registration.getSchedule())
                .target(registration.getTarget())
                .skills(skills)
                .introduction(introduction)
                .build();

        projectRepository.save(project);

        return project.getId();
    }


    public ProjectDetail getProject(Long id) {

        final Project project = projectRepository.findByIdAndType(id, MuchType.PROJECT)
                .orElseThrow(() -> new MuchException(Code.PROJECT_NOT_FOUND, String.format("프로젝트 없음 [%s] ", id)));

        final User projectWriter = project.getWriter();

        // 스킬 잘라내기
        final String skills = project.getSkills();
        List<ProjectDetail.SkillDetail> skillDetails = new ArrayList<>();

        Arrays.stream(skills.split(","))
                .forEach(s -> skillDetails.add(ProjectDetail.SkillDetail.builder()
                        .name(s)
                        .imageUrl("") // TODO 추후 이미지 URL 설정
                        .build()));

        // 포지션 상세 생성 + 포지션 총 인원 합/포지션 전체 필요 인원 합구하기 + 포지션별 인원 설정
//        final List<WorkPosition> workPositions = project.getWorkPositions();
//        List<MuchDetail.WorkDetail> workDetails = new ArrayList<>();
//
//        int currentSum = 0;
//        int needsSum = 0;
//        for (WorkPosition position : workPositions) {
//            Integer current = position.getCurrent();
//            Integer needs = position.getNeeds();
//
//            currentSum += current;
//            needsSum += needs;
//
//            workDetails.add(MuchDetail.WorkDetail.builder()
//                    .position(position.getPosition())
//                    .current(current)
//                    .needs(needs)
//                    .build());
//        }

        // 최종 조립 후 반환
        return ProjectDetail.builder()
                .id(project.getId())
                .title(project.getTitle())
                .writer(ProjectDetail.WriterDetail.builder()
                        .id(projectWriter.getId())
                        .nickname(projectWriter.getNickname())
                        .pictureUrl(projectWriter.getImageUrl())
                        .build())
                .imageUrl(project.getImageUrl())
                .isOnline(project.isOnline())
                .location(project.getLocation())
                .deadline(project.getDeadline())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .schedule(project.getSchedule())
                .target(project.getTarget())
//                        .currentTotal(currentSum)
//                        .maximumPeople(needsSum)
                .introduction(project.getIntroduction())
                .skills(skillDetails)
//                        .work(workDetails)
                .build();
    }

}
