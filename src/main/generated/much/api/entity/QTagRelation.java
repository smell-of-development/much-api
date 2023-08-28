package much.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTagRelation is a Querydsl query type for TagRelation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTagRelation extends EntityPathBase<TagRelation> {

    private static final long serialVersionUID = 953647456L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTagRelation tagRelation = new QTagRelation("tagRelation");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> relationId = createNumber("relationId", Long.class);

    public final EnumPath<much.api.common.enums.MuchType> relationType = createEnum("relationType", much.api.common.enums.MuchType.class);

    public final QTag tag;

    public QTagRelation(String variable) {
        this(TagRelation.class, forVariable(variable), INITS);
    }

    public QTagRelation(Path<? extends TagRelation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTagRelation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTagRelation(PathMetadata metadata, PathInits inits) {
        this(TagRelation.class, metadata, inits);
    }

    public QTagRelation(Class<? extends TagRelation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tag = inits.isInitialized("tag") ? new QTag(forProperty("tag")) : null;
    }

}

