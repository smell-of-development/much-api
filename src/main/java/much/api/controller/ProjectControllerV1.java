package much.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import much.api.controller.swagger.ProjectApiV1;
import much.api.dto.request.ProjectCreation;
import much.api.dto.response.Envelope;
import much.api.dto.response.ProjectDetail;
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
    @GetMapping("/projects/{id}")
    public ResponseEntity<Envelope<ProjectDetail>> getProject(@PathVariable Long id) {

        return ok(
                Envelope.ok(projectService.getProject(id))
        );
    }

}
