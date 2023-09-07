package much.api.entity;

import jakarta.persistence.*;
import lombok.*;
import much.api.common.enums.CommunityCategory;
import much.api.common.util.EditorUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "tb_community",
        indexes = {
                @Index(name = "tb_community_idx1", columnList = "category"),
        }
)
public class Community extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CommunityCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User author;

    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @Column(columnDefinition = "text")
    private String contentWithoutHtmlTags;

    // TODO
    private Long viewCount;

    @Builder
    private Community(User author,
                      CommunityCategory category,
                      String title,
                      String content) {

        this.author = author;
        this.category = category;
        this.title = title;
        this.content = content;
        this.contentWithoutHtmlTags = EditorUtils.removeHtmlTags(content);
    }

    public void modify(String title,
                       String content) {

        this.title = title;
        this.content = content;
    }

    public boolean isAuthor(Long userId) {

        return author.getId().equals(userId);
    }
}
