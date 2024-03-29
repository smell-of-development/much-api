package much.api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import much.api.common.enums.FileType;
import much.api.common.enums.ImageResizeType;
import much.api.common.enums.MuchType;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "tb_file",
        indexes = {
                @Index(name = "tb_file_idx1", columnList = "relationType, relationId"),
                @Index(name = "tb_file_idx2", columnList = "storedFilename"),
        }
)
public class File extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FileType type;

    @Enumerated(EnumType.STRING)
    private ImageResizeType imageResizeType;

    // 에디터 업로드 후 미등록 및 삭제 파일에 대한 관리용
    @Enumerated(EnumType.STRING)
    private MuchType relationType;

    private Long relationId;

    @ColumnDefault("1")
    private boolean released;

    private String extension;

    private String originalFilename;

    private String storedFilename;

    private String url;

    @Builder
    private File(FileType type,
                 ImageResizeType imageResizeType,
                 MuchType relationType,
                 Long relationId,
                 boolean released,
                 String extension,
                 String originalFilename,
                 String storedFilename,
                 String url) {

        this.type = type;
        this.imageResizeType = imageResizeType;
        this.relationType = relationType;
        this.relationId = relationId;
        this.released = released;
        this.extension = extension;
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.url = url;
    }
}


