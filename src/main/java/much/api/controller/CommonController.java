package much.api.controller;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.Code;
import much.api.common.exception.InvalidNicknameException;
import much.api.common.exception.RequiredException;
import much.api.common.util.ValidationChecker;
import much.api.controller.swagger.CommonApi;
import much.api.dto.response.Envelope;
import much.api.dto.response.PositionResponse;
import much.api.service.CommonService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


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


    @Override
    @GetMapping("/codes")
    public ResponseEntity<Envelope<Code[]>> retrieveCodes() {

        return ResponseEntity.ok(Envelope.ok(Code.values()));
    }


    @Override
    @GetMapping("/name-validation")
    public ResponseEntity<Envelope<Void>> retrieveDuplicatedNickname(@RequestParam String nickname) {

        if (!StringUtils.hasLength(nickname)) {
            throw new RequiredException("nickname");
        }
        if (!ValidationChecker.isValidNickname(nickname)) {
            throw new InvalidNicknameException();
        }

        commonService.checkDuplicatedNickname(nickname);

        return ResponseEntity.ok(Envelope.empty());
    }

}
