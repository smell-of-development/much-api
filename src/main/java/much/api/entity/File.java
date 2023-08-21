package much.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import much.api.common.enums.FileType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "TB_FILE")
public class File extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private FileType type;

    private String originalFilename;

    private String storedFilename;

    private String storedPath;

}
