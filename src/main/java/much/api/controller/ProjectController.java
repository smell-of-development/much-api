package much.api.controller;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.MuchType;
import much.api.controller.swagger.ProjectApi;
import much.api.dto.request.ProjectCreation;
import much.api.dto.response.Envelope;
import much.api.dto.response.ProjectDetail;
import much.api.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProjectController implements ProjectApi {

    private final ProjectService projectService;

    @Override
    @PostMapping("/project")
    public ResponseEntity<Envelope<Long>> createProject(@RequestBody ProjectCreation request) {

        return ResponseEntity.ok(projectService.createProject(request, MuchType.PROJECT));
    }


    @Override
    @GetMapping("/project/{id}")
    public ResponseEntity<Envelope<ProjectDetail>> getProject(@PathVariable Long id) {

        return ResponseEntity.ok(projectService.getProject(id));
    }

}
