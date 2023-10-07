package much.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProject is a Querydsl query type for Project
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProject extends EntityPathBase<Project> {

    private static final long serialVersionUID = 1469140419L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProject project = new QProject("project");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final StringPath address = createString("address");

    public final ListPath<ProjectApplication, QProjectApplication> applications = this.<ProjectApplication, QProjectApplication>createList("applications", ProjectApplication.class, QProjectApplication.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DatePath<java.time.LocalDate> deadline = createDate("deadline", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final StringPath introduction = createString("introduction");

    public final StringPath introductionWithoutHtmlTags = createString("introductionWithoutHtmlTags");

    public final BooleanPath online = createBoolean("online");

    public final ListPath<ProjectPosition, QProjectPosition> positionStatus = this.<ProjectPosition, QProjectPosition>createList("positionStatus", ProjectPosition.class, QProjectPosition.class, PathInits.DIRECT2);

    public final ListPath<ProjectJoin, QProjectJoin> projectMembers = this.<ProjectJoin, QProjectJoin>createList("projectMembers", ProjectJoin.class, QProjectJoin.class, PathInits.DIRECT2);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final StringPath timesPerWeek = createString("timesPerWeek");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public final QUser writer;

    public QProject(String variable) {
        this(Project.class, forVariable(variable), INITS);
    }

    public QProject(Path<? extends Project> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProject(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProject(PathMetadata metadata, PathInits inits) {
        this(Project.class, metadata, inits);
    }

    public QProject(Class<? extends Project> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.writer = inits.isInitialized("writer") ? new QUser(forProperty("writer")) : null;
    }

}

