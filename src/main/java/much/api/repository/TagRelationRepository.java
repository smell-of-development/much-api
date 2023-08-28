package much.api.repository;

import much.api.entity.TagRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRelationRepository extends JpaRepository<TagRelation, Long> {
}
