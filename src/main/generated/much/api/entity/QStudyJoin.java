package much.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStudyJoin is a Querydsl query type for StudyJoin
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStudyJoin extends EntityPathBase<StudyJoin> {

    private static final long serialVersionUID = 2033930941L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStudyJoin studyJoin = new QStudyJoin("studyJoin");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QUser member;

    public final QStudy study;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QStudyJoin(String variable) {
        this(StudyJoin.class, forVariable(variable), INITS);
    }

    public QStudyJoin(Path<? extends StudyJoin> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStudyJoin(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStudyJoin(PathMetadata metadata, PathInits inits) {
        this(StudyJoin.class, metadata, inits);
    }

    public QStudyJoin(Class<? extends StudyJoin> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QUser(forProperty("member")) : null;
        this.study = inits.isInitialized("study") ? new QStudy(forProperty("study"), inits.get("study")) : null;
    }

}

