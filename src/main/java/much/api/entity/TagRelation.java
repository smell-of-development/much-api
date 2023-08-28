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
        name = "TB_TAG_RELATION",
        indexes = {
                @Index(name = "IDX__RELATION", columnList = "relationType, relationId"),
                @Index(name = "IDX__TAG_ID", columnList = "tag"),
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
