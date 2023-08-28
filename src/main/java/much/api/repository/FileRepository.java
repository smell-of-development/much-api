package much.api.repository;

import much.api.common.enums.MuchType;
import much.api.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByStoredFilename(String storedFilename);

    @Query("UPDATE File f " +
            "SET f.relationType = :relationType" +
            "  , f.relationId = :relationId " +
            "WHERE f.storedFilename IN (:storedFilenames)")
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void updateRelationInformation(@Param("relationType") MuchType relationType,
                                   @Param("relationId") Long relationId,
                                   @Param("storedFilenames") List<String> storedFilenames);

}
