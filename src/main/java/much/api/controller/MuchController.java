package much.api.controller;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.MuchType;
import much.api.controller.swagger.MuchApi;
import much.api.dto.request.MuchRegistration;
import much.api.dto.response.Envelope;
import much.api.dto.response.MuchDetail;
import much.api.service.MuchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MuchController implements MuchApi {

    private final MuchService muchService;

    @Override
    @PostMapping("/project")
    public ResponseEntity<Envelope<Long>> registerProject(@RequestBody MuchRegistration request) {

        return ResponseEntity.ok(muchService.registerMuch(request, MuchType.PROJECT));
    }


    @Override
    @GetMapping("/project/{id}")
    public ResponseEntity<Envelope<MuchDetail>> retrieveProject(@PathVariable Long id) {

        return ResponseEntity.ok(muchService.retrieveProject(id));
    }

}
