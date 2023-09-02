package much.api.repository;

import much.api.common.enums.MuchType;
import much.api.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("UPDATE File f " +
            "SET f.relationType = :relationType" +
            "  , f.relationId   = :relationId " +
            "  , f.released     = false " +
            "WHERE f.storedFilename IN (:storedFilenames)")
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void setRelationByFilenames(@Param("relationType") MuchType relationType,
                                @Param("relationId") Long relationId,
                                @Param("storedFilenames") List<String> storedFilenames);


    @Query("UPDATE File f " +
            "SET f.released = true " +
            "WHERE f.relationType = :relationType " +
            "AND   f.relationId   = :relationId " +
            "AND   f.storedFilename NOT IN (:storedFilenames)")
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void releaseRelationByFilenamesNotIn(@Param("relationType") MuchType relationType,
                                         @Param("relationId") Long relationId,
                                         @Param("storedFilenames") List<String> storedFilenames);


    @Query("UPDATE File f " +
            "SET f.released = true " +
            "WHERE f.relationType = :relationType " +
            "AND   f.relationId   = :relationId")
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void releaseAllByRelation(@Param("relationType") MuchType relationType,
                              @Param("relationId") Long relationId);

}
