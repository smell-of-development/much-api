package much.api.controller;

import lombok.RequiredArgsConstructor;
import much.api.controller.swagger.StudyApiV1;
import much.api.dto.request.StudyApplicationForm;
import much.api.dto.request.StudyForm;
import much.api.dto.request.StudySearch;
import much.api.dto.response.*;
import much.api.service.StudyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/studies")
public class StudyControllerV1 implements StudyApiV1 {

    private final StudyService studyService;

    @Override
    @PostMapping
    public ResponseEntity<Envelope<Long>> createStudy(@RequestBody StudyForm request) {

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

    @Override
    @DeleteMapping("/{studyId}")
    public ResponseEntity<Envelope<Void>> deleteStudy(@PathVariable Long studyId) {

        studyService.deleteStudy(studyId);
        return ok(
                Envelope.empty()
        );
    }

    @Override
    @PostMapping("/{studyId}/applications")
    public ResponseEntity<Envelope<Void>> createStudyApplication(@PathVariable Long studyId,
                                                                 @RequestBody StudyApplicationForm request) {

        studyService.createStudyApplication(studyId, request);
        return ok(
                Envelope.empty()
        );
    }

    @Override
    @DeleteMapping("/{studyId}/applications")
    public ResponseEntity<Envelope<Void>> deleteStudyApplication(@PathVariable Long studyId) {

        studyService.deleteStudyApplication(studyId);
        return ok(
                Envelope.empty()
        );
    }

    @Override
    @GetMapping("/{studyId}/applications")
    public ResponseEntity<Envelope<List<StudyApplication>>> getStudyApplications(@PathVariable Long studyId) {

        return ok(
                Envelope.ok(studyService.getStudyApplications(studyId))
        );
    }

    @Override
    @PostMapping("/applications/{applicationId}/accept")
    public ResponseEntity<Envelope<Void>> acceptStudyApplication(@PathVariable Long applicationId) {

        studyService.acceptStudyApplication(applicationId);
        return ok(
                Envelope.empty()
        );
    }

}
