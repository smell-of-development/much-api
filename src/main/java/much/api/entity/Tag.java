package much.api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "tb_tag",
        indexes = {
                @Index(name = "tb_tag_idx1", columnList = "name", unique = true),
        }
)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Tag(String name) {
        this.name = name;
    }

    public static Tag ofName(String name) {

        return new Tag(name);
    }

}
