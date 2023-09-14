package much.api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import much.api.common.enums.CommunityCategory;
import much.api.common.exception.NoAuthority;
import much.api.common.util.ContextUtils;
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
                @Index(name = "tb_community_idx1", columnList = "id, category", unique = true),
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
    private long viewCount;

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

        if (!isAuthor()) {
            throw new NoAuthority("게시글 수정");
        }
        this.title = title;
        this.content = content;
    }

    public boolean isAuthor() {

        Long contextUserId = ContextUtils.getUserId();
        return author.getId().equals(contextUserId);
    }


    public void increaseViewCount() {

        ++viewCount;
    }

}
