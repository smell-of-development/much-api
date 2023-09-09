package much.api.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import much.api.common.enums.CommunityCategory;
import much.api.dto.request.CommunitySearch;
import much.api.dto.response.CommunityPostSummary;
import much.api.dto.response.PageElement;
import much.api.entity.Community;
import much.api.entity.QCommunity;
import much.api.entity.QTagRelation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.*;
import static much.api.common.enums.MuchType.COMMUNITY;
import static much.api.entity.QCommunity.community;
import static much.api.entity.QTagRelation.tagRelation;
import static much.api.entity.QUser.user;

@Slf4j
@Repository
public class CommunitySearchRepository extends QuerydslRepositorySupport {

    public CommunitySearchRepository() {
        super(Community.class);
    }

    public Page<CommunitySearchDto> searchCommunityPosts(CommunitySearch searchCondition) {

        if (!StringUtils.hasText(searchCondition.getSearch()) && searchCondition.getTags().isEmpty()) {
            return Page.empty();
        }

        // TODO 제목 및 내용검색 - 인메모리 DB 에서 MySQL FULLTEXT 미지원
        if (StringUtils.hasText(searchCondition.getSearch())) {
            return Page.empty();
        }

        PageRequest pageRequest = PageRequest.of(searchCondition.getPage(), searchCondition.getSize());

        QTagRelation subTr = new QTagRelation("subTR");
        QTagRelation countTr = new QTagRelation("countTR");
        QCommunity subC = new QCommunity("subC");

        /*
        태그검색 쿼리
        SELECT   c.id,                       -- 게시글 PK
                 c.author_id,                -- 작성자 PK
                (SELECT nickname
                 FROM   user
                 WHERE  id = c.author_id),   -- 작성자 닉네임
                (SELECT image_url
                 FROM   user
                 WHERE  id = c.author_id),   -- 작성자 사진
                (SELECT category
                 FROM   community
                 WHERE  id = c.id),          -- 게시글 카테고리
                (SELECT title
                 FROM   community
                 WHERE  id = c.id),          -- 게시글 제목
                (SELECT content_without_html_tags
                 FROM   community
                 WHERE  id = c.id),          -- 태그가 제외된 게시글 본문
                (SELECT created_at
                 FROM   community
                 WHERE  id = c.id),          -- 작성일
                (SELECT COUNT(id)
                 FROM   tag_relation
                 WHERE  relation_type = 'COMMUNITY'
                 AND    relation_id = c.id
                 AND    tag_name IN (?...)), -- 검색한 태그가 포함된 수 TODO 포함수/게시글태그수(비율)?
                 GROUP_CONCAT(c.id)          -- 구분자 ','으로 이어진 게시글의 전체 태그
        FROM     tag_relation tr
        JOIN     community c
        ON       tr.relation_type = 'COMMUNITY' AND tr.relation_id = c.id AND c.category = ?
        WHERE    EXISTS (SELECT 1
                         FROM   tag_relation sub_tr
                         WHERE  sub_tr.relation_type = 'COMMUNITY'
                         AND    sub_tr.relation_id   = c.id
                         AND    sub_tr.tag_name     IN (?...))
        GROUP BY c.id, c.author_id
        ORDER BY 9 DESC, c.id DESC
         */
        return applyPagination(pageRequest,
                qf -> select(Projections.constructor(CommunitySearchDto.class,
                        community.id,
                        community.author.id,
                        JPAExpressions.select(user.nickname).from(user).where(user.id.eq(community.author.id)),
                        JPAExpressions.select(user.imageUrl).from(user).where(user.id.eq(community.author.id)),
                        JPAExpressions.select(subC.category).from(subC).where(subC.id.eq(community.id)),
                        JPAExpressions.select(subC.title).from(subC).where(subC.id.eq(community.id)),
                        JPAExpressions.select(subC.contentWithoutHtmlTags).from(subC).where(subC.id.eq(community.id)),
                        JPAExpressions.select(subC.createdAt).from(subC).where(subC.id.eq(community.id)),
                        as(JPAExpressions
                                .select(countTr.count())
                                .from(countTr)
                                .where(countTr.relationType.eq(COMMUNITY)
                                        .and(countTr.relationId.eq(community.id))
                                        .and(countTr.tagName.in(searchCondition.getTags()))), "tagHitCount"),
                        stringTemplate("GROUP_CONCAT({0})", tagRelation.tagName))
                )
                        .from(tagRelation)
                        .join(community)
                        .on(tagRelation.relationType.eq(COMMUNITY)
                                .and(tagRelation.relationId.eq(community.id))
                                .and(community.category.eq(searchCondition.getCategory())))
                        .where(JPAExpressions
                                .selectFrom(subTr)
                                .where(subTr.relationType.eq(COMMUNITY)
                                        .and(subTr.relationId.eq(community.id))
                                        .and(subTr.tagName.in(searchCondition.getTags()))).exists())
                        .groupBy(community.id, community.author.id)
                        .orderBy(orderSpecifiers(searchCondition.isByRecent()))
                ,

                // 페이징을 위한 COUNT 쿼리
                qf -> select(tagRelation.relationId)
                        .from(tagRelation)
                        .join(community)
                        .on(tagRelation.relationType.eq(COMMUNITY)
                                .and(tagRelation.relationId.eq(community.id))
                                .and(community.category.eq(searchCondition.getCategory())))
                        .where(JPAExpressions
                                .selectFrom(subTr)
                                .where(subTr.relationType.eq(COMMUNITY)
                                        .and(subTr.relationId.eq(community.id))
                                        .and(subTr.tagName.in(searchCondition.getTags()))).exists())
                        .groupBy(community.id, community.author.id)
        );
    }


    private OrderSpecifier<?>[] orderSpecifiers(boolean byRecent) {

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (!byRecent) {
            orderSpecifiers.add(stringPath("tagHitCount").desc());
        }

        orderSpecifiers.add(community.id.desc());
        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }

    @Getter
    public static class CommunitySearchDto implements PageElement<CommunityPostSummary> {

        private static final int MAX_CONTENT_LENGTH = 50;

        private Long id;

        private Long authorId;

        private String authorNickname;

        private String authorImageUrl;

        private CommunityCategory category;

        private String title;

        private String content;

        private String tags;

        // TODO
        private Long viewCount;

        // TODO
        private Long commentCount;

        private LocalDateTime createdAt;

        private long tagHitCount;


        public CommunitySearchDto(Long id,
                                  Long authorId,
                                  String authorNickname,
                                  String authorImageUrl,
                                  CommunityCategory category,
                                  String title,
                                  String content,
                                  LocalDateTime createdAt,
                                  Long tagHitCount,
                                  String tags) {

            this.id = id;
            this.authorId = authorId;
            this.authorNickname = authorNickname;
            this.authorImageUrl = authorImageUrl;
            this.category = category;
            this.title = title;
            this.content = (content != null && content.length() >= MAX_CONTENT_LENGTH) ?
                    content.substring(0, MAX_CONTENT_LENGTH) + "..." : content;
            this.tags = tags;
            this.createdAt = createdAt;
            this.tagHitCount = tagHitCount;
        }


        @Override
        public CommunityPostSummary toResponseDto() {

            return CommunityPostSummary.builder()
                    .id(id)
                    .category(category.name())
                    .title(title)
                    .content(content)
                    .tags(List.of(tags.split(",")))
                    .authorId(authorId)
                    .authorNickname(authorNickname)
                    .authorImageUrl(authorImageUrl)
                    .createdAt(createdAt)
                    .build();
        }
    }
}
