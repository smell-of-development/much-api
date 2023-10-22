package much.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProjectPosition is a Querydsl query type for ProjectPosition
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProjectPosition extends EntityPathBase<ProjectPosition> {

    private static final long serialVersionUID = -327034868L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProjectPosition projectPosition = new QProjectPosition("projectPosition");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> needs = createNumber("needs", Integer.class);

    public final ListPath<ProjectApplication, QProjectApplication> positionApplications = this.<ProjectApplication, QProjectApplication>createList("positionApplications", ProjectApplication.class, QProjectApplication.class, PathInits.DIRECT2);

    public final ListPath<ProjectJoin, QProjectJoin> positionJoins = this.<ProjectJoin, QProjectJoin>createList("positionJoins", ProjectJoin.class, QProjectJoin.class, PathInits.DIRECT2);

    public final QProject project;

    public final NumberPath<Integer> recruited = createNumber("recruited", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QProjectPosition(String variable) {
        this(ProjectPosition.class, forVariable(variable), INITS);
    }

    public QProjectPosition(Path<? extends ProjectPosition> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProjectPosition(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProjectPosition(PathMetadata metadata, PathInits inits) {
        this(ProjectPosition.class, metadata, inits);
    }

    public QProjectPosition(Class<? extends ProjectPosition> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.project = inits.isInitialized("project") ? new QProject(forProperty("project"), inits.get("project")) : null;
    }

}

