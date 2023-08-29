package much.api.controller;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.MuchType;
import much.api.controller.swagger.ProjectApiV1;
import much.api.dto.request.ProjectCreation;
import much.api.dto.response.Envelope;
import much.api.dto.response.ProjectDetail;
import much.api.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProjectControllerV1 implements ProjectApiV1 {

    private final ProjectService projectService;

    @Override
    @PostMapping("/projects")
    public ResponseEntity<Envelope<Long>> createProject(@RequestBody ProjectCreation request) {

        return ok(
                Envelope.ok(projectService.createProject(request, MuchType.PROJECT))
        );
    }


    @Override
    @GetMapping("/projects/{id}")
    public ResponseEntity<Envelope<ProjectDetail>> getProject(@PathVariable Long id) {

        return ok(
                Envelope.ok(projectService.getProject(id))
        );
    }

}
