package much.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSmsCertificationHist is a Querydsl query type for SmsCertificationHist
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSmsCertificationHist extends EntityPathBase<SmsCertificationHist> {

    private static final long serialVersionUID = -516907975L;

    public static final QSmsCertificationHist smsCertificationHist = new QSmsCertificationHist("smsCertificationHist");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final BooleanPath certified = createBoolean("certified");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath number = createString("number");

    public final StringPath phoneNumber = createString("phoneNumber");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSmsCertificationHist(String variable) {
        super(SmsCertificationHist.class, forVariable(variable));
    }

    public QSmsCertificationHist(Path<? extends SmsCertificationHist> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSmsCertificationHist(PathMetadata metadata) {
        super(SmsCertificationHist.class, metadata);
    }

}

