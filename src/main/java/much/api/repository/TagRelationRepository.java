package much.api.repository;

import much.api.common.enums.MuchType;
import much.api.entity.Tag;
import much.api.entity.TagRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface TagRelationRepository extends JpaRepository<TagRelation, Long> {

    @Query("DELETE FROM TagRelation tr " +
            "WHERE tr.relationType = :relationType " +
            "AND   tr.relationId = :relationId " +
            "AND   tr.tag NOT IN (:tags)")
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void deleteByTagNotIn(@Param("relationType") MuchType relationType,
                          @Param("relationId") Long relationId,
                          @Param("tags") Collection<Tag> tags);

}
