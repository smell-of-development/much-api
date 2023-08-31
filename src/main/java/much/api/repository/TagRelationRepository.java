package much.api.repository;

import much.api.common.enums.MuchType;
import much.api.entity.TagRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface TagRelationRepository extends JpaRepository<TagRelation, Long> {

    @Query("SELECT tr " +
            "FROM TagRelation tr " +
            "JOIN FETCH tr.tag " +
            "WHERE tr.relationType = :relationType " +
            "AND   tr.relationId = :relationId")
    Set<TagRelation> findAllByRelation(@Param("relationType") MuchType relationType,
                                       @Param("relationId") Long relationId);

}
