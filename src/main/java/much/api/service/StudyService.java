package much.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import much.api.common.aop.MuchValid;
import much.api.common.exception.StudyNotFound;
import much.api.common.util.ContextUtils;
import much.api.dto.request.StudyForm;
import much.api.dto.request.StudySearch;
import much.api.dto.response.PagedResult;
import much.api.dto.response.StudyDetail;
import much.api.dto.response.StudySummary;
import much.api.entity.Study;
import much.api.entity.User;
import much.api.repository.StudyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static much.api.common.enums.MuchType.PROJECT;
import static much.api.common.enums.MuchType.STUDY;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyService {

    private final CommonService commonService;

    private final StudyRepository studyRepository;

    private final FileService fileService;

    private final TagHelperService tagHelperService;

    @Transactional
    public Long createStudy(@MuchValid StudyForm studyForm) {

        Long userId = ContextUtils.getUserId();
        User user = commonService.getUserOrThrowException(userId);

        List<String> requestMeetingDays = studyForm.getMeetingDays();

        String meetingDays = requestMeetingDays.isEmpty() ? null :
                String.join(", ", requestMeetingDays);

        String requestIntroduction = studyForm.getIntroduction();
        Set<String> requestTags = studyForm.getTags();

        Study study = Study.builder()
                .writer(user)
                .title(studyForm.getTitle())
                .imageUrl(studyForm.getImageUrl())
                .online(studyForm.getOnline())
                .address(studyForm.getAddress())
                .deadline(studyForm.getDeadline())
                .startDate(studyForm.getStartDate())
                .endDate(studyForm.getEndDate())
                .meetingDays(meetingDays)
                .needs(studyForm.getNeeds())
                .introduction(requestIntroduction)
                .build();

        Study saved = studyRepository.save(study);

        // 파일 관리정보 업데이트
        fileService.handleEditorImage(STUDY, saved.getId(), requestIntroduction);

        // 태그정보, 관계정보 반영
        tagHelperService.handleTagRelation(STUDY, saved.getId(), requestTags);

        return saved.getId();
    }


    public Long modifyStudy(Long studyId,
                            @MuchValid StudyForm studyForm) {

        // 사용자 확인
        Long userId = ContextUtils.getUserId();
        commonService.getUserOrThrowException(userId);

        // 기존글 조회
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFound(studyId));

        List<String> requestMeetingDays = studyForm.getMeetingDays();

        String timesPerWeek = requestMeetingDays.isEmpty() ? null :
                String.join(",", requestMeetingDays);

        String modifiedIntroduction = studyForm.getIntroduction();

        study.modify(
                studyForm.getTitle(),
                studyForm.getImageUrl(),
                studyForm.getOnline(),
                studyForm.getAddress(),
                studyForm.getDeadline(),
                studyForm.getStartDate(),
                studyForm.getEndDate(),
                timesPerWeek,
                studyForm.getNeeds(),
                modifiedIntroduction
        );

        // 태그정보 수정
        Set<String> tags = studyForm.getTags();
        tagHelperService.handleTagRelation(STUDY, studyId, tags);

        // 파일정보 수정
        fileService.handleEditorImage(PROJECT, studyId, modifiedIntroduction);

        return studyId;
    }


    public StudyDetail getStudy(Long studyId) {

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFound(studyId));

        Set<String> tags = tagHelperService.getTags(STUDY, studyId);

        return StudyDetail.ofEntity(study, tags);
    }


    public PagedResult<StudySummary> getStudies(StudySearch searchCondition) {

        return null;
    }
}
