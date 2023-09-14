package much.api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import much.api.common.enums.MuchType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "tb_tag_relation",
        indexes = {
                @Index(name = "tb_tag_relation_idx1", columnList = "relationType, relationId")
        }
)
public class TagRelation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MuchType relationType;

    private Long relationId;

    private String tagName;


    private TagRelation(MuchType relationType,
                        Long relationId,
                        String tagName) {

        this.relationType = relationType;
        this.relationId = relationId;
        this.tagName = tagName;
    }


    public static TagRelation ofTypeAndId(MuchType relationType,
                                          Long relationId,
                                          String tagName) {

        return new TagRelation(relationType, relationId, tagName);
    }

}
