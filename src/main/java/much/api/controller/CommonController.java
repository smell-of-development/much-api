package much.api.controller;

import lombok.RequiredArgsConstructor;
import much.api.controller.swagger.CommonApi;
import much.api.dto.response.Envelope;
import much.api.dto.response.PositionResponse;
import much.api.service.CommonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/common")
public class CommonController implements CommonApi {

    private final CommonService commonService;


    @Override
    @GetMapping("/positions")
    public ResponseEntity<Envelope<PositionResponse>> retrievePositions() {

        return ResponseEntity.ok(commonService.retrievePositions());
    }

}
