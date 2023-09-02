package much.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFile is a Querydsl query type for File
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFile extends EntityPathBase<File> {

    private static final long serialVersionUID = 1289487122L;

    public static final QFile file = new QFile("file");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath extension = createString("extension");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<much.api.common.enums.ImageResizeType> imageResizeType = createEnum("imageResizeType", much.api.common.enums.ImageResizeType.class);

    public final StringPath originalFilename = createString("originalFilename");

    public final NumberPath<Long> relationId = createNumber("relationId", Long.class);

    public final EnumPath<much.api.common.enums.MuchType> relationType = createEnum("relationType", much.api.common.enums.MuchType.class);

    public final BooleanPath released = createBoolean("released");

    public final StringPath storedFilename = createString("storedFilename");

    public final EnumPath<much.api.common.enums.FileType> type = createEnum("type", much.api.common.enums.FileType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath url = createString("url");

    public QFile(String variable) {
        super(File.class, forVariable(variable));
    }

    public QFile(Path<? extends File> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFile(PathMetadata metadata) {
        super(File.class, metadata);
    }

}

