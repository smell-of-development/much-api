package much.api.repository;

import much.api.entity.ProjectApplication;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ProjectApplicationRepository extends JpaRepository<ProjectApplication, Long> {

    @NonNull
    @Override
    @EntityGraph(attributePaths = {"project", "position", "member"})
    Optional<ProjectApplication> findById(@NonNull Long id);

    Optional<ProjectApplication> findByProjectIdAndMemberId(Long projectId, Long memberId);

    int deleteByProjectIdAndMemberId(Long projectId, Long memberId);

}
