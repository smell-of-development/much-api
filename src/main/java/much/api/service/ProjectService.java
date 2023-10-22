package much.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import much.api.common.aop.MuchValid;
import much.api.common.exception.*;
import much.api.common.util.ContextUtils;
import much.api.dto.request.ProjectApplicationCreation;
import much.api.dto.request.ProjectCreation;
import much.api.dto.request.ProjectModification;
import much.api.dto.request.ProjectSearch;
import much.api.dto.response.PagedResult;
import much.api.dto.response.ProjectApplication;
import much.api.dto.response.ProjectDetail;
import much.api.dto.response.ProjectSummary;
import much.api.entity.Project;
import much.api.entity.ProjectPosition;
import much.api.entity.User;
import much.api.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static much.api.common.enums.MuchType.PROJECT;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final CommonService commonService;

    private final ProjectRepository projectRepository;

    private final ProjectPositionRepository projectPositionRepository;

    private final ProjectSearchRepository projectSearchRepository;

    private final ProjectJoinRepository projectJoinRepository;

    private final ProjectApplicationRepository projectApplicationRepository;

    private final FileService fileService;

    private final TagHelperService tagHelperService;

    @Transactional
    public Long createProject(@MuchValid ProjectCreation projectCreation) {

        Long userId = ContextUtils.getUserId();
        User user = commonService.getUserOrThrowException(userId);

        // 저장
        List<String> requestMeetingDays = projectCreation.getMeetingDays();

        String meetingDays = requestMeetingDays.isEmpty() ? null :
                String.join(",", requestMeetingDays);

        final String requestIntroduction = projectCreation.getIntroduction();
        final Set<String> requestTags = projectCreation.getTags();

        Project project = Project.builder()
                .writer(user)
                .title(projectCreation.getTitle())
                .imageUrl(projectCreation.getImageUrl())
                .online(projectCreation.getOnline())
                .address(projectCreation.getAddress())
                .deadline(projectCreation.getDeadline())
                .startDate(projectCreation.getStartDate())
                .endDate(projectCreation.getEndDate())
                .meetingDays(meetingDays)
                .introduction(requestIntroduction)
                .build();

        Project saved = projectRepository.save(project);

        // 포지션 정보 등록
        ProjectCreation.Recruit recruit = projectCreation.getRecruit();
        for (ProjectCreation.Recruit.PositionStatus ps : recruit.getPositionStatus()) {

            ProjectPosition projectPosition = ProjectPosition.builder()
                    .project(saved)
                    .name(ps.getName())
                    .needs(ps.getNeeds())
                    .build();

            saved.addPosition(projectPosition, ps.isContainsMe());
        }

        // 파일 관리정보 업데이트
        fileService.handleEditorImage(PROJECT, saved.getId(), requestIntroduction);

        // 태그정보, 관계정보 반영
        tagHelperService.handleTagRelation(PROJECT, saved.getId(), requestTags);

        return saved.getId();
    }


    public ProjectDetail getProject(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFound(projectId));

        Set<String> tags = tagHelperService.getTags(PROJECT, projectId);

        project.increaseViewCount();
        return ProjectDetail.ofEntity(project, tags);
    }


    @Transactional
    public void deleteProject(Long projectId) {

        // 사용자 확인
        Long userId = ContextUtils.getUserId();
        commonService.getUserOrThrowException(userId);

        // 프로젝트 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFound(projectId));

        if (!project.isWriter()) {
            throw new NoAuthority("프로젝트 삭제");
        }

        // 태그정보 삭제
        tagHelperService.deleteTagRelation(PROJECT, projectId);

        // 파일정보 관리
        fileService.releaseEditorImage(PROJECT, projectId);

        // 삭제 - CaseCade
        projectRepository.delete(project);
    }


    @Transactional
    public ProjectDetail modifyProject(Long projectId,
                                       @MuchValid ProjectModification request) {

        // 사용자 확인
        Long userId = ContextUtils.getUserId();
        commonService.getUserOrThrowException(userId);

        // 기존글 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFound(projectId));

        // 수정
        List<String> requestMeetingDays = request.getMeetingDays();

        String timesPerWeek = requestMeetingDays.isEmpty() ? null :
                String.join(",", requestMeetingDays);

        final String modifiedIntroduction = request.getIntroduction();

        project.modify(
                request.getTitle(),
                request.getImageUrl(),
                request.getOnline(),
                request.getAddress(),
                request.getDeadline(),
                request.getStartDate(),
                request.getEndDate(),
                timesPerWeek,
                modifiedIntroduction
        );

        // 포지션 정보 변경
        handlePosition(project, request.getRecruit().getPositionStatus());

        // 태그정보 수정
        final Set<String> tags = request.getTags();
        tagHelperService.handleTagRelation(PROJECT, projectId, tags);

        // 파일정보 수정
        fileService.handleEditorImage(PROJECT, projectId, modifiedIntroduction);

        return ProjectDetail.ofEntity(project, tags);
    }


    public PagedResult<ProjectSummary> getProjects(ProjectSearch searchCondition) {

        Long userId = ContextUtils.getUserId();
        Page<ProjectSearchRepository.ProjectSearchDto> page = projectSearchRepository.searchProjects(searchCondition, userId);

        // 결과 PROJECT ID 리스트
        List<Long> projectIds = page.stream()
                .map(ProjectSearchRepository.ProjectSearchDto::getId)
                .toList();

        // 결과 ID 이용해 포지션 정보 획득
        Map<Long, List<ProjectPosition>> projectPositionMap = projectPositionRepository.findAllByProjectIdIn(projectIds)
                .stream()
                .collect(groupingBy(pp -> pp.getProject().getId()));

        // 포지션 정보 조립
        Page<ProjectSummary> mappedPage = page.map(ProjectSearchRepository.ProjectSearchDto::toResponseDto);

        mappedPage.getContent()
                .forEach(ps -> ps.setRecruit(
                        ProjectDetail.Recruit.of(projectPositionMap.get(ps.getId())))
                );

        // noinspection unchecked
        return (PagedResult<ProjectSummary>) PagedResult.ofPageWithCompletelyMapped(mappedPage);
    }


    @Transactional
    public void createProjectApplication(Long projectId,
                                         @MuchValid ProjectApplicationCreation applicationForm) {

        // 신청자 확인
        Long userId = ContextUtils.getUserId();
        User user = commonService.getUserOrThrowException(userId);

        // 프로젝트 확인
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFound(projectId));

        // 이미 참여중 유저인지 확인
        projectJoinRepository.findByProjectIdAndMemberId(projectId, userId)
                .ifPresent(pj -> {
                    throw new AlreadyJoinedProject();
                });

        // 이미 신청한 유저인지 확인
        projectApplicationRepository.findByProjectIdAndMemberId(projectId, userId)
                .ifPresent(pa -> {
                    throw new AlreadyAppliedProject();
                });

        // 프로젝트 포지션 확인
        Long positionId = applicationForm.getPositionId();

        ProjectPosition matchedPosition = project.getPositionStatus().stream()
                .filter(pp -> pp.getId().equals(positionId))
                .findAny()
                .orElseThrow(() -> new ProjectPositionNotFound(positionId));

        if (matchedPosition.isClosed()) {
            throw new AlreadyRecruitedPosition(matchedPosition.getName());
        }

        // 신청서 생성, 저장
        much.api.entity.ProjectApplication application = much.api.entity.ProjectApplication.builder()
                .project(project)
                .position(matchedPosition)
                .member(user)
                .memo(applicationForm.getMemo())
                .build();

        matchedPosition.getPositionApplications().add(application);
    }


    @Transactional
    public void deleteProjectApplication(Long projectId) {

        // 신청자 확인
        Long userId = ContextUtils.getUserId();
        commonService.getUserOrThrowException(userId);

        int deleted = projectApplicationRepository.deleteByProjectIdAndMemberId(projectId, userId);
        if (deleted == 0) {
            throw new ProjectApplicationNotFound();
        }
    }


    public List<ProjectApplication> getProjectApplications(Long projectId) {

        Long userId = ContextUtils.getUserId();
        if (userId == null) {
            throw new NoAuthority("신청서 목록 가져오기");
        }

        Optional<User> userOptional = commonService.getUser(userId);
        if (userOptional.isEmpty()) {
            throw new NoAuthority("신청서 목록 가져오기");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFound(projectId));

        Long writer = project.getWriter().getId();
        if (!writer.equals(userId)) {
            throw new NoAuthority("신청서 목록 가져오기");
        }

        // 프로젝트 생성자 == 요청자
        return project.getApplications()
                .stream()
                .map(ProjectApplication::ofEntity)
                .toList();
    }


    @Transactional
    public void acceptProjectApplication(Long applicationId) {

        // 사용자 확인
        Long userId = ContextUtils.getUserId();
        commonService.getUserOrThrowException(userId);

        // 신청서 확인
        much.api.entity.ProjectApplication application = projectApplicationRepository.findById(applicationId)
                .orElseThrow(ProjectApplicationNotFound::new);

        // 승인자, 신청서 프로젝트의 생성자 일치 확인
        Project project = application.getProject();

        User projectWriter = project.getWriter();
        assert userId != null;
        if (!userId.equals(projectWriter.getId())) {
            throw new NoAuthority("신청서 승인");
        }

        // 신청서의 포지션이 프로젝트에 존재하는지 확인
        ProjectPosition applicationPosition = application.getPosition();

        boolean positionMatched = project.getPositionStatus()
                .stream()
                .anyMatch(pp -> pp.equals(applicationPosition));

        if (!positionMatched) {
            throw new ProjectPositionNotFound(applicationPosition.getId());
        }

        // 포지션 모집완료 확인
        if (applicationPosition.isClosed()) {
            throw new AlreadyRecruitedPosition(applicationPosition.getName());
        }

        // 승인(등록)
        application.accept();
    }


    private void handlePosition(Project project,
                                List<ProjectModification.Recruit.PositionStatus> tobe) {

        // 기존 포지션 Map
        Map<Long, ProjectPosition> asisPositionMap = project.getPositionStatus()
                .stream()
                .collect(toMap(ProjectPosition::getId, pp -> pp));

        tobe.forEach(tobePosition -> {

            Long key = tobePosition.getId();
            ProjectPosition asisPosition = asisPositionMap.get(key);

            // 기존 포지션 ID
            if (asisPosition != null) {
                // 이름과 인원수 변경
                asisPosition.modify(
                        tobePosition.getName(),
                        tobePosition.getNeeds(),
                        tobePosition.isContainsMe() // 기존 포지션 유지 OR 기존 -> 기존 다른 포지션
                );

                // 완료된 것 Map remove
                asisPositionMap.remove(key);
            } else {
                // 새로운 포지션 -> 등록
                project.addPosition(
                        ProjectPosition.builder()
                                .project(project)
                                .name(tobePosition.getName())
                                .needs(tobePosition.getNeeds())
                                .build(),
                        tobePosition.isContainsMe() // 기존 포지션 -> 새로운 포지션
                );
            }

        });

        // 미처리 된 나머지 -> 삭제 될 포지션
        // 삭제. 신청서 또한 삭제됨
        project.deletePosition(asisPositionMap.values());
    }

}
