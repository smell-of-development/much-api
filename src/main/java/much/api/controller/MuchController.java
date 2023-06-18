package much.api.controller;

import lombok.RequiredArgsConstructor;
import much.api.controller.swagger.MuchApi;
import much.api.dto.request.MuchRegistration;
import much.api.dto.response.Envelope;
import much.api.service.MuchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MuchController implements MuchApi {

    private final MuchService muchService;

    @Override
    @PostMapping("/project")
    public ResponseEntity<Envelope<Void>> registerProject(@RequestBody MuchRegistration request) {

        return ResponseEntity.ok(muchService.registerMuch(request));
    }

}
