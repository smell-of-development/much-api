package much.api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import much.api.common.enums.FileType;
import much.api.common.enums.ImageResizeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "TB_FILE",
        indexes = {@Index(name = "IDX__STORED_FILENAME", columnList = "storedFilename")}
)
public class File extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FileType type;

    @Enumerated(EnumType.STRING)
    private ImageResizeType imageResizeType;

    // 게시글 타입-ID 형태 ex) PROJECT-1 / STUDY-1 / COMMUNITY-1
    private String relationInfo;

    private String extension;

    private String originalFilename;

    private String storedFilename;

    private String url;

    @Builder
    private File(FileType type,
                 ImageResizeType imageResizeType,
                 String relationInfo,
                 String extension,
                 String originalFilename,
                 String storedFilename,
                 String url) {

        this.type = type;
        this.imageResizeType = imageResizeType;
        this.relationInfo = relationInfo;
        this.extension = extension;
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.url = url;
    }
}
