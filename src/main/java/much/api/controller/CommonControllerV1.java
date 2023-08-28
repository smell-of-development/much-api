package much.api.controller;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.ImageResizeType;
import much.api.common.enums.SkillTag;
import much.api.controller.swagger.CommonApiV1;
import much.api.dto.response.Envelope;
import much.api.service.CommonService;
import much.api.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/common")
public class CommonControllerV1 implements CommonApiV1 {

    private final CommonService commonService;

    private final FileService fileService;


    @Override
    @GetMapping("/id-validation")
    public ResponseEntity<Envelope<Void>> checkDuplicatedLoginId(@RequestParam String id) {

        commonService.checkDuplicatedLoginId(id);
        return ok(
                Envelope.empty()
        );
    }

    @Override
    @GetMapping("/name-validation")
    public ResponseEntity<Envelope<Void>> checkDuplicatedNickname(@RequestParam String nickname) {

        commonService.checkDuplicatedNickname(nickname);
        return ok(
                Envelope.empty()
        );
    }

    @Override
    @GetMapping("/skill-tags")
    public ResponseEntity<Envelope<Map<String, String>>> getSkillTags(@RequestParam(required = false) String name) {

        Map<String, String> response = new HashMap<>();

        if (StringUtils.hasText(name)) {

            Arrays.stream(SkillTag.values())
                    .filter(skillTag -> {

                        String skill = skillTag.getEnglishName().toLowerCase().replace(" ", "");
                        String searchWord = name.toLowerCase().replace(" ", "");

                        return skill.contains(searchWord) || skillTag.getKoreanName().contains(searchWord);
                    })
                    .forEach(skillTag -> response.put(skillTag.name(), skillTag.getEnglishName()));
        } else {
            Arrays.stream(SkillTag.values())
                    .forEach(skillTag -> response.put(skillTag.name(), skillTag.getEnglishName()));
        }

        return ok(
                Envelope.ok(response)
        );
    }

    @Override
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Envelope<String>> uploadImage(@RequestPart MultipartFile image,
                                                        @RequestParam(defaultValue = "NONE") ImageResizeType type) {

        return ok(
                Envelope.ok(fileService.uploadImage(image, type))
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
