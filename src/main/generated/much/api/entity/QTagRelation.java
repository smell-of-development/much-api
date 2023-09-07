package much.api.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTagRelation is a Querydsl query type for TagRelation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTagRelation extends EntityPathBase<TagRelation> {

    private static final long serialVersionUID = 953647456L;

    public static final QTagRelation tagRelation = new QTagRelation("tagRelation");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> relationId = createNumber("relationId", Long.class);

    public final EnumPath<much.api.common.enums.MuchType> relationType = createEnum("relationType", much.api.common.enums.MuchType.class);

    public final StringPath tagName = createString("tagName");

    public QTagRelation(String variable) {
        super(TagRelation.class, forVariable(variable));
    }

    public QTagRelation(Path<? extends TagRelation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTagRelation(PathMetadata metadata) {
        super(TagRelation.class, metadata);
    }

}

