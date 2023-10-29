package much.api.repository;

import much.api.entity.StudyApplication;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface StudyApplicationRepository extends JpaRepository<StudyApplication, Long> {

    @NonNull
    @Override
    @EntityGraph(attributePaths = {"study", "member"})
    Optional<StudyApplication> findById(@NonNull Long id);

    Optional<StudyApplication> findByStudyIdAndMemberId(Long studyId, Long memberId);

    int deleteByStudyIdAndMemberId(Long studyId, Long memberId);

}
