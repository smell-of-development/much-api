package much.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDevParameter is a Querydsl query type for DevParameter
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDevParameter extends EntityPathBase<DevParameter> {

    private static final long serialVersionUID = 2144480938L;

    public static final QDevParameter devParameter = new QDevParameter("devParameter");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final BooleanPath useYn = createBoolean("useYn");

    public QDevParameter(String variable) {
        super(DevParameter.class, forVariable(variable));
    }

    public QDevParameter(Path<? extends DevParameter> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDevParameter(PathMetadata metadata) {
        super(DevParameter.class, metadata);
    }

}

