package much.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import much.api.controller.swagger.ProjectApiV1;
import much.api.dto.request.ProjectCreation;
import much.api.dto.request.ProjectModification;
import much.api.dto.request.ProjectSearch;
import much.api.dto.response.Envelope;
import much.api.dto.response.PagedResult;
import much.api.dto.response.ProjectDetail;
import much.api.dto.response.ProjectSummary;
import much.api.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProjectControllerV1 implements ProjectApiV1 {

    private final ProjectService projectService;

    @Override
    @PostMapping("/projects")
    public ResponseEntity<Envelope<Long>> createProject(@RequestBody @Valid ProjectCreation request) {

        return ok(
                Envelope.ok(projectService.createProject(request))
        );
    }


    @Override
    @PutMapping("/projects/{projectId}")
    public ResponseEntity<Envelope<Long>> modifyProject(@PathVariable Long projectId,
                                                                 @RequestBody @Valid ProjectModification request) {

        return ok(
                Envelope.ok(projectService.modifyProject(projectId, request).getId())
        );
    }


    @Override
    @GetMapping("/projects/{projectId}")
    public ResponseEntity<Envelope<ProjectDetail>> getProject(@PathVariable Long projectId) {

        return ok(
                Envelope.ok(projectService.getProject(projectId))
        );
    }

    @Override
    @GetMapping("/projects")
    public ResponseEntity<Envelope<PagedResult<ProjectSummary>>> getProjects(ProjectSearch searchCondition) {

        return ok(
                Envelope.ok(projectService.getProjects(searchCondition))
        );
    }


    @Override
    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<Envelope<Void>> deleteProject(@PathVariable Long projectId) {

        projectService.deleteProject(projectId);
        return ok(
                Envelope.empty()
        );
    }

}
