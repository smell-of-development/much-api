package much.api.controller;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.Code;
import much.api.controller.swagger.CommonApi;
import much.api.dto.response.Envelope;
import much.api.dto.response.Positions;
import much.api.service.CommonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/common")
public class CommonController implements CommonApi {

    private final CommonService commonService;

    @Override
    @GetMapping("/positions")
    public ResponseEntity<Envelope<Positions>> retrievePositions() {

        return ResponseEntity.ok(commonService.retrievePositions());
    }


    @Override
    @GetMapping("/codes")
    public ResponseEntity<Envelope<Code[]>> retrieveCodes() {

        return ResponseEntity.ok(Envelope.ok(Code.values()));
    }

    @Override
    @GetMapping("/id-validation")
    public ResponseEntity<Envelope<Void>> retrieveDuplicatedLoginId(@RequestParam String id) {

        commonService.checkDuplicatedLoginId(id);
        return ResponseEntity.ok(Envelope.empty());
    }

    @Override
    @GetMapping("/name-validation")
    public ResponseEntity<Envelope<Void>> retrieveDuplicatedNickname(@RequestParam String nickname) {

        commonService.checkDuplicatedNickname(nickname);
        return ResponseEntity.ok(Envelope.empty());
    }

}
