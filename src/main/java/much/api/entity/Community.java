package much.api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import much.api.common.enums.CommunityCategory;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "TB_COMMUNITY",
        indexes = {
                @Index(name = "IDX__CATEGORY", columnList = "category"),
        }
)
public class Community extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @Enumerated(EnumType.STRING)
    private CommunityCategory category;

    private String content;

    private String tags;

    @Builder
    private Community(User author,
                      CommunityCategory category,
                      String content,
                      String tags) {

        this.author = author;
        this.category = category;
        this.content = content;
        this.tags = tags;
    }
}
