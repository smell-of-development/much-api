package much.api.controller;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.Code;
import much.api.common.enums.ImageResizeType;
import much.api.common.enums.Skill;
import much.api.controller.swagger.CommonApiV1;
import much.api.dto.response.Envelope;
import much.api.service.CommonService;
import much.api.service.FileService;
import much.api.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/common")
public class CommonControllerV1 implements CommonApiV1 {

    private final CommonService commonService;

    private final UserService userService;

    private final FileService fileService;


    @Override
    @GetMapping("/codes")
    public ResponseEntity<Envelope<Code[]>> retrieveCodes() {

        return ok(
                Envelope.ok(Code.values())
        );
    }

    @Override
    @GetMapping("/id-validation")
    public ResponseEntity<Envelope<Void>> retrieveDuplicatedLoginId(@RequestParam String id) {

        userService.checkDuplicatedLoginId(id);
        return ok(
                Envelope.empty()
        );
    }

    @Override
    @GetMapping("/name-validation")
    public ResponseEntity<Envelope<Void>> retrieveDuplicatedNickname(@RequestParam String nickname) {

        userService.checkDuplicatedNickname(nickname);
        return ok(
                Envelope.empty()
        );
    }

    @Override
    @GetMapping("/skills")
    public ResponseEntity<Envelope<List<String>>> retrieveSkills(@RequestParam String name) {

        return ok(
                Envelope.ok(
                        Arrays.stream(Skill.values())
                                .filter(skill -> skill.getEnglishName()
                                        .replace(" ", "")
                                        .toLowerCase()
                                        .contains(name.toLowerCase().replace(" ", ""))
                                        || skill.getKoreanName().contains(name))
                                .map(Skill::getEnglishName)
                                .toList())
        );
    }

    @Override
    @PostMapping(value = "/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Envelope<String>> uploadImage(@RequestPart MultipartFile image,
                                                        @RequestParam(defaultValue = "NONE") String type) {

        return ok(
                Envelope.ok(fileService.uploadImage(image, ImageResizeType.valueOf(type.toUpperCase())))
        );
    }

    @Override
    @GetMapping("/image/{storedFilename}")
    public ResponseEntity<Resource> getLocalImage(@PathVariable String storedFilename) {

        FileService.LocalImage localImage = fileService.getLocalImage(storedFilename);

        return ok()
                .header(CONTENT_TYPE, localImage.getContentType())
                .body(localImage.getResource());
    }

}
