package much.api.controller;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.Code;
import much.api.common.enums.ImageResizeType;
import much.api.common.enums.Skill;
import much.api.common.util.ContextUtils;
import much.api.common.util.FileStore;
import much.api.controller.swagger.CommonApi;
import much.api.dto.response.Envelope;
import much.api.dto.response.Positions;
import much.api.service.CommonService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/common")
public class CommonController implements CommonApi {

    private final CommonService commonService;

    private final FileStore fileStore;

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

    @Override
    @GetMapping("/skills")
    public ResponseEntity<Envelope<List<String>>> retrieveSkills(@RequestParam(defaultValue = "") String name) {

        return ResponseEntity.ok(Envelope.ok(
                Arrays.stream(Skill.values())
                        .filter(skill -> skill.getEnglishName().toLowerCase().contains(name.toLowerCase())
                                || skill.getKoreanName().contains(name))
                        .map(Skill::getEnglishName)
                        .toList()));
    }

    @Override
    @PostMapping(value = "/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Envelope<String>> uploadImage(@RequestPart MultipartFile image,
                                                        @RequestParam(defaultValue = "none") String type) {

        return ResponseEntity.ok(Envelope.ok(
                ContextUtils.getHost()
                        + "/common/image"
                        + fileStore.uploadImage(image, ImageResizeType.valueOf(type.toUpperCase()))));
    }

    @Override
    @GetMapping("/image/{path}/{storedFilename}")
    public ResponseEntity<Resource> retrieveImage(@PathVariable String path,
                                                  @PathVariable String storedFilename) throws IOException {

        // TODO FileNotFoundException 생각하기

        if ("temp".equals(path)) {
            String fullPath = fileStore.getImagePath() + File.separator + storedFilename;
            MediaType mediaType = MediaType.parseMediaType(Files.probeContentType(Paths.get(fullPath)));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, mediaType.toString())
                    .body(new FileSystemResource(new File(fullPath)));
        }

        return null;
    }

}
