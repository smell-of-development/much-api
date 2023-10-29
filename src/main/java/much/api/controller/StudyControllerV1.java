package much.api.controller;

import lombok.RequiredArgsConstructor;
import much.api.controller.swagger.StudyApiV1;
import much.api.dto.request.StudyForm;
import much.api.dto.request.StudySearch;
import much.api.dto.response.Envelope;
import much.api.dto.response.PagedResult;
import much.api.dto.response.StudyDetail;
import much.api.dto.response.StudySummary;
import much.api.service.StudyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/studies")
public class StudyControllerV1 implements StudyApiV1 {

    private final StudyService studyService;

    @Override
    @PostMapping
    public ResponseEntity<Envelope<Long>> createStudy(StudyForm request) {

        return ok(
                Envelope.ok(studyService.createStudy(request))
        );
    }

    @Override
    @PutMapping("/{studyId}")
    public ResponseEntity<Envelope<Long>> modifyStudy(@PathVariable Long studyId,
                                                      @RequestBody StudyForm request) {

        return ok(
                Envelope.ok(studyService.modifyStudy(studyId, request))
        );
    }

    @Override
    @GetMapping("/{studyId}")
    public ResponseEntity<Envelope<StudyDetail>> getStudy(@PathVariable Long studyId) {

        return ok(
                Envelope.ok(studyService.getStudy(studyId))
        );
    }

    @Override
    @GetMapping
    public ResponseEntity<Envelope<PagedResult<StudySummary>>> getStudies(StudySearch searchCondition) {

        return ok(
                Envelope.ok(studyService.getStudies(searchCondition))
        );
    }

}
