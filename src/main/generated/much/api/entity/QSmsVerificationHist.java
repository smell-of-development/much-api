package much.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSmsVerificationHist is a Querydsl query type for SmsVerificationHist
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSmsVerificationHist extends EntityPathBase<SmsVerificationHist> {

    private static final long serialVersionUID = 529803008L;

    public static final QSmsVerificationHist smsVerificationHist = new QSmsVerificationHist("smsVerificationHist");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath number = createString("number");

    public final StringPath phoneNumber = createString("phoneNumber");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final BooleanPath verified = createBoolean("verified");

    public QSmsVerificationHist(String variable) {
        super(SmsVerificationHist.class, forVariable(variable));
    }

    public QSmsVerificationHist(Path<? extends SmsVerificationHist> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSmsVerificationHist(PathMetadata metadata) {
        super(SmsVerificationHist.class, metadata);
    }

}

