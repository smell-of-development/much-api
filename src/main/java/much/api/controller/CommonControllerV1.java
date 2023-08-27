package much.api.controller;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.Code;
import much.api.common.enums.ImageResizeType;
import much.api.common.enums.Skill;
import much.api.common.util.ContextUtils;
import much.api.common.util.FileStore;
import much.api.controller.swagger.CommonApiV1;
import much.api.dto.response.Envelope;
import much.api.service.CommonService;
import much.api.service.UserService;
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
@RequestMapping("/api/v1/common")
public class CommonControllerV1 implements CommonApiV1 {

    private final CommonService commonService;

    private final UserService userService;

    private final FileStore fileStore;


    @Override
    @GetMapping("/codes")
    public ResponseEntity<Envelope<Code[]>> retrieveCodes() {

        return ResponseEntity.ok(Envelope.ok(Code.values()));
    }

    @Override
    @GetMapping("/id-validation")
    public ResponseEntity<Envelope<Void>> retrieveDuplicatedLoginId(@RequestParam String id) {

        userService.checkDuplicatedLoginId(id);
        return ResponseEntity.ok(Envelope.empty());
    }

    @Override
    @GetMapping("/name-validation")
    public ResponseEntity<Envelope<Void>> retrieveDuplicatedNickname(@RequestParam String nickname) {

        userService.checkDuplicatedNickname(nickname);
        return ResponseEntity.ok(Envelope.empty());
    }

    @Override
    @GetMapping("/skills")
    public ResponseEntity<Envelope<List<String>>> retrieveSkills(@RequestParam String name) {

        return ResponseEntity.ok(Envelope.ok(
                Arrays.stream(Skill.values())
                        .filter(skill -> skill.getEnglishName()
                                                .replace(" ", "")
                                                .toLowerCase()
                                                .contains(name.toLowerCase().replace(" ", ""))
                                || skill.getKoreanName().contains(name))
                        .map(Skill::getEnglishName)
                        .toList()));
    }

    @Override
    @PostMapping(value = "/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Envelope<String>> uploadImage(@RequestPart MultipartFile image,
                                                        @RequestParam(defaultValue = "none") String type) {

        // TODO 업로드시 DB 관리 서비스레이어 만들기
        return ResponseEntity.ok(Envelope.ok(
                ContextUtils.getHost()
                        + "/common/image/"
                        + fileStore.uploadImage(image, ImageResizeType.valueOf(type.toUpperCase()))));
    }

    @Override
    @GetMapping("/image/{storedFilename}")
    public ResponseEntity<Resource> retrieveImage(@PathVariable String storedFilename) throws IOException {

        // TODO FileNotFoundException 생각하기

        String fullPath = fileStore.getImagePath() + File.separator + storedFilename;
        MediaType mediaType = MediaType.parseMediaType(Files.probeContentType(Paths.get(fullPath)));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, mediaType.toString())
                .body(new FileSystemResource(new File(fullPath)));
    }

}
