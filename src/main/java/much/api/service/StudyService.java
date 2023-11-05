package much.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import much.api.common.aop.MuchValid;
import much.api.common.exception.*;
import much.api.common.util.ContextUtils;
import much.api.dto.request.StudyApplicationForm;
import much.api.dto.request.StudyForm;
import much.api.dto.request.StudySearch;
import much.api.dto.response.PagedResult;
import much.api.dto.response.StudyDetail;
import much.api.dto.response.StudySummary;
import much.api.entity.Study;
import much.api.entity.StudyApplication;
import much.api.entity.User;
import much.api.repository.StudyApplicationRepository;
import much.api.repository.StudyJoinRepository;
import much.api.repository.StudyRepository;
import much.api.repository.StudySearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static much.api.common.enums.MuchType.STUDY;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyService {

    private final CommonService commonService;

    private final StudyRepository studyRepository;

    private final StudySearchRepository studySearchRepository;

    private final StudyJoinRepository studyJoinRepository;

    private final StudyApplicationRepository studyApplicationRepository;

    private final FileService fileService;

    private final TagHelperService tagHelperService;

    @Transactional
    public Long createStudy(@MuchValid StudyForm studyForm) {

        Long userId = ContextUtils.getUserId();
        User user = commonService.getUserOrThrowException(userId);

        List<String> requestMeetingDays = studyForm.getMeetingDays();

        String meetingDays = requestMeetingDays.isEmpty() ? null :
                String.join(",", requestMeetingDays);

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
        saved.addMember(user);

        // 파일 관리정보 업데이트
        fileService.handleEditorImage(STUDY, saved.getId(), requestIntroduction);

        // 태그정보, 관계정보 반영
        tagHelperService.handleTagRelation(STUDY, saved.getId(), requestTags);

        return saved.getId();
    }


    @Transactional
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
        fileService.handleEditorImage(STUDY, studyId, modifiedIntroduction);

        return studyId;
    }


    public StudyDetail getStudy(Long studyId) {

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFound(studyId));

        Set<String> tags = tagHelperService.getTags(STUDY, studyId);

        return StudyDetail.ofEntity(study, tags);
    }


    public PagedResult<StudySummary> getStudies(StudySearch searchCondition) {

        Long userId = ContextUtils.getUserId();
        Page<StudySearchRepository.StudySearchDto> page = studySearchRepository.searchStudies(searchCondition, userId);

        //noinspection unchecked
        return (PagedResult<StudySummary>) PagedResult.ofPageWithNotMapped(page);
    }


    @Transactional
    public void deleteStudy(Long studyId) {

        // 사용자 확인
        Long userId = ContextUtils.getUserId();
        commonService.getUserOrThrowException(userId);

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFound(studyId));

        if (!study.isWriter()) {
            throw new NoAuthority("스터디 삭제");
        }

        // 태그정보 삭제
        tagHelperService.deleteTagRelation(STUDY, studyId);

        // 파일정보 관리
        fileService.releaseEditorImage(STUDY, studyId);

        studyRepository.delete(study);
    }


    @Transactional
    public void createStudyApplication(Long studyId,
                                       @MuchValid StudyApplicationForm applicationForm) {

        // 신청자 확인
        Long userId = ContextUtils.getUserId();
        User user = commonService.getUserOrThrowException(userId);

        // 스터디 확인
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFound(studyId));

        // 이미 참여중인 유저인지 확인
        studyJoinRepository.findByStudyIdAndMemberId(studyId, userId)
                .ifPresent(sj -> {
                    throw new AlreadyJoined();
                });

        // 이미 신청한 유저인지 확인
        studyApplicationRepository.findByStudyIdAndMemberId(studyId, userId)
                .ifPresent(sj -> {
                    throw new AlreadyApplied();
                });

        // 스터디 모집완료 확인
        if (study.isClosed()) {
            throw new AlreadyRecruited();
        }

        // 신청서 생성, 저장
        StudyApplication application = StudyApplication.builder()
                .study(study)
                .member(user)
                .memo(applicationForm.getMemo())
                .build();

        studyApplicationRepository.save(application);
    }


    @Transactional
    public void deleteStudyApplication(Long studyId) {

        // 신청자 확인
        Long userId = ContextUtils.getUserId();
        commonService.getUserOrThrowException(userId);

        int deleted = studyApplicationRepository.deleteByStudyIdAndMemberId(studyId, userId);
        if (deleted == 0) {
            throw new ApplicationFormNotFound();
        }
    }

    public List<much.api.dto.response.StudyApplication> getStudyApplications(Long studyId) {

        Long userId = ContextUtils.getUserId();
        if (userId == null) {
            throw new NoAuthority("신청서 목록 가져오기");
        }

        commonService.getUserOrThrowException(userId);

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFound(studyId));

        if (!study.isWriter()) {
            throw new NoAuthority("신청서 목록 가져오기");
        }

        return study.getApplications()
                .stream()
                .map(much.api.dto.response.StudyApplication::ofEntity)
                .toList();
    }


    @Transactional
    public void acceptStudyApplication(Long applicationId) {

        // 사용자 확인
        Long userId = ContextUtils.getUserId();
        commonService.getUserOrThrowException(userId);

        // 신청서 확인
        StudyApplication application = studyApplicationRepository.findById(applicationId)
                .orElseThrow(ApplicationFormNotFound::new);

        // 스터디
        Study study = application.getStudy();

        // 스터디 모집완료 확인
        if (study.isClosed()) {
            throw new AlreadyRecruited();
        }

        // 승인(등록)
        application.accept();
    }

}
