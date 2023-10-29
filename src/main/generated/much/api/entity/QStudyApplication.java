package much.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStudyApplication is a Querydsl query type for StudyApplication
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStudyApplication extends EntityPathBase<StudyApplication> {

    private static final long serialVersionUID = -414600099L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStudyApplication studyApplication = new QStudyApplication("studyApplication");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QUser member;

    public final StringPath memo = createString("memo");

    public final QStudy study;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QStudyApplication(String variable) {
        this(StudyApplication.class, forVariable(variable), INITS);
    }

    public QStudyApplication(Path<? extends StudyApplication> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStudyApplication(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStudyApplication(PathMetadata metadata, PathInits inits) {
        this(StudyApplication.class, metadata, inits);
    }

    public QStudyApplication(Class<? extends StudyApplication> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QUser(forProperty("member")) : null;
        this.study = inits.isInitialized("study") ? new QStudy(forProperty("study"), inits.get("study")) : null;
    }

}

