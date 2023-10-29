package much.api.repository;

import much.api.entity.StudyJoin;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyJoinRepository extends JpaRepository<StudyJoin, Long> {

    @EntityGraph(attributePaths = {"study", "member"})
    Optional<StudyJoin> findByStudyIdAndMemberId(Long studyId, Long memberId);

}
