package much.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProjectJoin is a Querydsl query type for ProjectJoin
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProjectJoin extends EntityPathBase<ProjectJoin> {

    private static final long serialVersionUID = 1862403469L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProjectJoin projectJoin = new QProjectJoin("projectJoin");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QUser member;

    public final QProjectPosition position;

    public final QProject project;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QProjectJoin(String variable) {
        this(ProjectJoin.class, forVariable(variable), INITS);
    }

    public QProjectJoin(Path<? extends ProjectJoin> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProjectJoin(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProjectJoin(PathMetadata metadata, PathInits inits) {
        this(ProjectJoin.class, metadata, inits);
    }

    public QProjectJoin(Class<? extends ProjectJoin> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QUser(forProperty("member")) : null;
        this.position = inits.isInitialized("position") ? new QProjectPosition(forProperty("position"), inits.get("position")) : null;
        this.project = inits.isInitialized("project") ? new QProject(forProperty("project"), inits.get("project")) : null;
    }

}

