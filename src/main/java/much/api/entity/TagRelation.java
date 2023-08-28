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
                @Index(name = "tb_tag_relation_idx1", columnList = "relationType, relationId"),
                @Index(name = "tb_tag_relation_idx2", columnList = "tag_id"),
        }
)
public class TagRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MuchType relationType;

    private Long relationId;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;


    private TagRelation(MuchType relationType,
                        Long relationId,
                        Tag tag) {

        this.relationType = relationType;
        this.relationId = relationId;
        this.tag = tag;
    }


    public static TagRelation ofTypeAndId(MuchType relationType,
                                          Long relationId,
                                          Tag tag) {

        return new TagRelation(relationType, relationId, tag);
    }

}
