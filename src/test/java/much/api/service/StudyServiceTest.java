package much.api.service;

import much.api.WithUser;
import much.api.common.exception.InvalidPeriod;
import much.api.common.exception.NoAuthority;
import much.api.dto.request.StudyForm;
import much.api.dto.response.StudyDetail;
import much.api.entity.Study;
import much.api.entity.TagRelation;
import much.api.entity.User;
import much.api.repository.StudyRepository;
import much.api.repository.TagRelationRepository;
import much.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static much.api.common.enums.MuchType.STUDY;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudyServiceTest {

    @Autowired
    StudyService studyService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    TagRelationRepository tagRelationRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
        studyRepository.deleteAll();
        tagRelationRepository.deleteAll();
    }


    static class StudyFormAggregator implements ArgumentsAggregator {

        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

            return StudyForm.builder()
                    .title(accessor.getString(0))
                    .online(Boolean.getBoolean(accessor.getString(1)))
                    .address(accessor.getString(2))
                    .deadline(accessor.getString(3) == null ? null : LocalDate.parse(accessor.getString(3), formatter))
                    .startDate(accessor.getString(4) == null ? null : LocalDate.parse(accessor.getString(4), formatter))
                    .endDate(accessor.getString(5) == null ? null : LocalDate.parse(accessor.getString(5), formatter))
                    .meetingDays(accessor.getString(6) == null ? List.of() : List.of(accessor.getString(6).split("\\|")))
                    .needs(accessor.getInteger(7))
                    .tags(accessor.getString(8) == null ? Set.of() : Set.of(accessor.getString(8).split("\\|")))
                    .introduction(accessor.getString(9))
                    .build();
        }

    }

    @ParameterizedTest
    @DisplayName("스터디 등록 성공")
    @CsvSource({
            "제목 1, true, , 2023.11.01, 2023.11.01, 2023.12.01, , 5, JAVA|JPA, 소개 1",
            "제목 2, false, 서울시 동대문구, 2023.12.01, 2023.12.01, 2024.01.01, 월|수|금, 2, , 소개 2",
            "제목 3, true, , 2023.11.01, 2023.12.07, 2024.12.01, , 5, Spring|JPA, 소개 3",
            "제목 4, true, , 2023.11.01, , , , 3, , 소개 4",
    })
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void study_create_test1(@AggregateWith(StudyFormAggregator.class) StudyForm studyForm) {

        // when
        Long studyId = studyService.createStudy(studyForm);

        // then
        Optional<Study> byId = studyRepository.findById(studyId);

        assertTrue(byId.isPresent());

        Study saved = byId.get();
        Set<String> tags = tagRelationRepository.findAllByRelation(STUDY, studyId)
                .stream()
                .map(TagRelation::getTagName)
                .collect(Collectors.toSet());

        assertEquals(studyId, saved.getId());
        assertEquals(studyForm.getOnline(), saved.isOnline());
        assertEquals(studyForm.getAddress(), saved.getAddress());
        assertEquals(studyForm.getDeadline(), saved.getDeadline());
        assertEquals(studyForm.getStartDate(), saved.getStartDate());
        assertEquals(studyForm.getEndDate(), saved.getEndDate());
        assertEquals(studyForm.getMeetingDays().isEmpty() ? null : String.join(",", studyForm.getMeetingDays()), saved.getMeetingDays());
        assertEquals(studyForm.getNeeds(), saved.getNeeds());
        assertEquals(studyForm.getTags(), tags);
        assertEquals(studyForm.getIntroduction(), saved.getIntroduction());
    }

    @ParameterizedTest
    @DisplayName("스터디 등록 실패 - 일정 입력 이상")
    @CsvSource({
            "제목 1, true, , 2023.11.01, , 2023.12.01, , 5, JAVA|JPA, 소개 1",
            "제목 2, false, 서울시 동대문구, 2023.12.01, , 2024.01.01, 월|수|금, 2, , 소개 2",
            "제목 3, true, , 2023.11.01, 2023.12.07, 2023.12.05, , 5, Spring|JPA, 소개 3",
    })
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void study_create_test2(@AggregateWith(StudyFormAggregator.class) StudyForm studyForm) {

        // expected
        assertThrows(InvalidPeriod.class, () -> studyService.createStudy(studyForm));
    }


    @ParameterizedTest
    @DisplayName("스터디 수정 성공")
    @CsvSource({
            "제목 1, true, , 2023.11.01, 2023.11.01, 2023.12.01, , 5, JAVA|JPA, 소개 1",
            "제목 2, false, 서울시 동대문구, 2023.12.01, 2023.12.01, 2024.01.01, 월|수|금, 2, , 소개 2",
            "제목 3, true, , 2023.11.01, 2023.12.07, 2024.12.01, , 5, Spring|JPA, 소개 3",
            "제목 4, true, , 2023.11.01, , , , 3, , 소개 4",
    })
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void study_modify_test1(@AggregateWith(StudyFormAggregator.class) StudyForm studyForm) {

        // given
        StudyForm existing = StudyForm.builder()
                .title("기존 글")
                .online(true)
                .address("서울")
                .deadline(LocalDate.of(2023, 11, 1))
                .meetingDays(List.of())
                .needs(2)
                .tags(Set.of())
                .introduction("기존 글")
                .build();

        Long existingStudyId = studyService.createStudy(existing);

        // when
        Long modifiedStudyId = studyService.modifyStudy(existingStudyId, studyForm);

        // then
        Optional<Study> byId = studyRepository.findById(modifiedStudyId);

        assertTrue(byId.isPresent());

        Study modified = byId.get();
        Set<String> tags = tagRelationRepository.findAllByRelation(STUDY, modifiedStudyId)
                .stream()
                .map(TagRelation::getTagName)
                .collect(Collectors.toSet());

        assertEquals(existingStudyId, modifiedStudyId);
        assertEquals(studyForm.getOnline(), modified.isOnline());
        assertEquals(studyForm.getAddress(), modified.getAddress());
        assertEquals(studyForm.getDeadline(), modified.getDeadline());
        assertEquals(studyForm.getStartDate(), modified.getStartDate());
        assertEquals(studyForm.getEndDate(), modified.getEndDate());
        assertEquals(studyForm.getMeetingDays().isEmpty() ? null : String.join(",", studyForm.getMeetingDays()), modified.getMeetingDays());
        assertEquals(studyForm.getNeeds(), modified.getNeeds());
        assertEquals(studyForm.getTags(), tags);
        assertEquals(studyForm.getIntroduction(), modified.getIntroduction());
    }


    @ParameterizedTest
    @DisplayName("스터디 수정 실패 - 작성자가 아님")
    @CsvSource({
            "제목 1, true, , 2023.11.01, 2023.11.01, 2023.12.01, , 5, JAVA|JPA, 소개 1"
    })
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void study_modify_test2(@AggregateWith(StudyFormAggregator.class) StudyForm studyForm) {

        // given

        // 다른 사용자, 기존 게시글
        User otherUser = User.builder()
                .loginId("otherUser")
                .build();
        userRepository.save(otherUser);

        Study existingStudy = studyRepository.save(
                Study.builder()
                        .writer(otherUser)
                        .title("기존 글")
                        .online(true)
                        .address("서울")
                        .deadline(LocalDate.of(2023, 11, 1))
                        .needs(2)
                        .introduction("기존 글")
                        .build()
        );

        // expected
        assertThrows(NoAuthority.class, () -> studyService.modifyStudy(existingStudy.getId(), studyForm));
    }


    @Test
    @DisplayName("스터디 삭제 성공")
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void study_delete_test1() {

        // given
        StudyForm existing = StudyForm.builder()
                .title("기존 글")
                .online(true)
                .address("서울")
                .deadline(LocalDate.of(2023, 11, 1))
                .meetingDays(List.of())
                .needs(2)
                .tags(Set.of())
                .introduction("기존 글")
                .build();

        Long existingStudyId = studyService.createStudy(existing);

        // when
        studyService.deleteStudy(existingStudyId);

        // then
        assertTrue(studyRepository.findAll().isEmpty());
    }


    @Test
    @DisplayName("스터디 삭제 실패 - 작성자가 아님")
    @WithUser(loginId = "testUser", password = "pw", nickname = "테스트")
    void study_delete_test2() {

        // 다른 사용자, 기존 게시글
        User otherUser = User.builder()
                .loginId("otherUser")
                .build();
        userRepository.save(otherUser);

        Study existingStudy = studyRepository.save(
                Study.builder()
                        .writer(otherUser)
                        .title("기존 글")
                        .online(true)
                        .address("서울")
                        .deadline(LocalDate.of(2023, 11, 1))
                        .needs(2)
                        .introduction("기존 글")
                        .build()
        );

        // expected
        assertThrows(NoAuthority.class, () -> studyService.deleteStudy(existingStudy.getId()));
    }


    @Test
    @DisplayName("스터디 단건조회")
    void study_get_test1() {

        // given
        User otherUser = User.builder()
                .loginId("someOne")
                .build();
        userRepository.save(otherUser);

        Study existingStudy = studyRepository.save(
                Study.builder()
                        .writer(otherUser)
                        .title("기존 글")
                        .online(true)
                        .address("서울")
                        .deadline(LocalDate.of(2023, 11, 1))
                        .needs(2)
                        .introduction("기존 글")
                        .build()
        );

        // when
        StudyDetail studyDetail = studyService.getStudy(existingStudy.getId());

        // then
        assertNotNull(studyDetail.getWriter());
        assertEquals(existingStudy.getId(), studyDetail.getId());
        assertEquals(existingStudy.getTitle(), studyDetail.getTitle());
        assertEquals(existingStudy.isOnline(), studyDetail.isOnline());
        assertEquals(existingStudy.getDeadline(), studyDetail.getDeadline());
        assertNotNull(existingStudy.getRecruited());
    }
}