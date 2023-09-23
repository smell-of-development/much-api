package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.common.aop.MuchValid;
import much.api.common.exception.ProjectNotFound;
import much.api.common.util.ContextUtils;
import much.api.dto.request.ProjectCreation;
import much.api.dto.response.ProjectDetail;
import much.api.entity.Project;
import much.api.entity.ProjectPosition;
import much.api.entity.User;
import much.api.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static much.api.common.enums.MuchType.PROJECT;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final CommonService commonService;

    private final ProjectRepository projectRepository;

    private final FileService fileService;

    private final TagHelperService tagHelperService;

    @Transactional
    public Long createProject(@MuchValid ProjectCreation projectCreation) {

        Long userId = ContextUtils.getUserId();
        User user = commonService.getUserOrThrowException(userId);

        // 저장
        List<String> requestTimesPerWeek = projectCreation.getTimesPerWeek();

        String timesPerWeek = requestTimesPerWeek.isEmpty() ? "협의" :
                String.join(", ", requestTimesPerWeek);

        final String requestIntroduction = projectCreation.getIntroduction();
        final Set<String> requestTags = projectCreation.getTags();

        // TODO 등록자 본인의 포지션?
        Project project = Project.builder()
                .writer(user)
                .imageUrl(projectCreation.getImageUrl())
                .online(projectCreation.getOnline())
                .address(projectCreation.getAddress())
                .deadline(projectCreation.getDeadline())
                .startDate(projectCreation.getStartDate())
                .endDate(projectCreation.getEndDate())
                .timesPerWeek(timesPerWeek)
                .introduction(requestIntroduction)
                .build();

        Project saved = projectRepository.save(project);

        // 포지션정보 등록
        projectCreation.getRecruit().stream()
                .map(ps -> ProjectPosition.builder()
                        .project(saved)
                        .name(ps.getName())
                        .needs(ps.getNeeds())
                        .build())
                .forEach(pp -> saved.getPositionStatus().add(pp));

        // 파일 관리정보 업데이트
        fileService.handleEditorImage(PROJECT, saved.getId(), requestIntroduction);

        // 태그정보, 관계정보 반영
        tagHelperService.handleTagRelation(PROJECT, saved.getId(), requestTags);

        return saved.getId();
    }


    public ProjectDetail getProject(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFound(id));

        Set<String> tags = tagHelperService.getTags(PROJECT, id);

        project.increaseViewCount();
        return ProjectDetail.ofEntity(project, tags);
    }

}
