package much.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProjectApplication is a Querydsl query type for ProjectApplication
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProjectApplication extends EntityPathBase<ProjectApplication> {

    private static final long serialVersionUID = -18139251L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProjectApplication projectApplication = new QProjectApplication("projectApplication");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final BooleanPath approved = createBoolean("approved");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QUser member;

    public final QProjectPosition position;

    public final QProject project;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QProjectApplication(String variable) {
        this(ProjectApplication.class, forVariable(variable), INITS);
    }

    public QProjectApplication(Path<? extends ProjectApplication> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProjectApplication(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProjectApplication(PathMetadata metadata, PathInits inits) {
        this(ProjectApplication.class, metadata, inits);
    }

    public QProjectApplication(Class<? extends ProjectApplication> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QUser(forProperty("member")) : null;
        this.position = inits.isInitialized("position") ? new QProjectPosition(forProperty("position"), inits.get("position")) : null;
        this.project = inits.isInitialized("project") ? new QProject(forProperty("project"), inits.get("project")) : null;
    }

}

