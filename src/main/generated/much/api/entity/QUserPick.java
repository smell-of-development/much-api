package much.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserPick is a Querydsl query type for UserPick
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserPick extends EntityPathBase<UserPick> {

    private static final long serialVersionUID = -969190110L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserPick userPick = new QUserPick("userPick");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final BooleanPath available = createBoolean("available");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> targetId = createNumber("targetId", Long.class);

    public final EnumPath<much.api.common.enums.MuchType> targetType = createEnum("targetType", much.api.common.enums.MuchType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUser user;

    public QUserPick(String variable) {
        this(UserPick.class, forVariable(variable), INITS);
    }

    public QUserPick(Path<? extends UserPick> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserPick(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserPick(PathMetadata metadata, PathInits inits) {
        this(UserPick.class, metadata, inits);
    }

    public QUserPick(Class<? extends UserPick> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

