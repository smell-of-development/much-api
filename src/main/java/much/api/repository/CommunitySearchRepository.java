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

        return applyPagination(pageRequest,
                qf -> select(Projections.constructor(CommunitySearchDto.class,
                        community.id,
                        community.category,
                        community.title,
                        community.contentWithoutHtmlTags,
                        community.createdAt,
                        community.author.id,
                        JPAExpressions
                                .select(user.nickname)
                                .from(user)
                                .where(user.id.eq(community.author.id)),
                        JPAExpressions
                                .select(user.imageUrl)
                                .from(user)
                                .where(user.id.eq(community.author.id)),
                        as(JPAExpressions
                                .select(countTr.count())
                                .from(countTr)
                                .where(countTr.relationType.eq(COMMUNITY)
                                        .and(countTr.relationId.eq(tagRelation.relationId))
                                        .and(countTr.tagName.in(searchCondition.getTags()))), "tagHitCount"),
                        stringTemplate("GROUP_CONCAT({0})", tagRelation.tagName))
                )
                        .from(tagRelation)
                        .join(community)
                        .on(tagRelation.relationType.eq(COMMUNITY)
                                .and(community.category.eq(searchCondition.getCategory()))
                                .and(tagRelation.relationId.eq(community.id)))
                        .where(JPAExpressions
                                .selectFrom(subTr)
                                .where(subTr.relationType.eq(COMMUNITY)
                                        .and(subTr.relationId.eq(tagRelation.relationId))
                                        .and(subTr.tagName.in(searchCondition.getTags()))).exists())
                        .groupBy(community.id, community.category, community.title, community.author.id, community.createdAt)
                        .orderBy(orderSpecifiers(searchCondition.isByRecent()))
                ,
                qf -> select(tagRelation.relationId)
                        .from(tagRelation)
                        .join(community)
                        .on(tagRelation.relationType.eq(COMMUNITY)
                                .and(community.category.eq(searchCondition.getCategory()))
                                .and(tagRelation.relationId.eq(community.id)))
                        .join(community.author, user)
                        .where(JPAExpressions
                                .selectFrom(subTr)
                                .where(subTr.relationType.eq(COMMUNITY)
                                        .and(subTr.relationId.eq(tagRelation.relationId))
                                        .and(subTr.tagName.in(searchCondition.getTags()))).exists())
                        .groupBy(community.id)
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

        private CommunityCategory category;

        private String title;

        private String content;

        private String tags;

        private Long authorId;

        private String authorNickname;

        private String authorImageUrl;

        // TODO
        private Long viewCount;

        // TODO
        private Long commentCount;

        private LocalDateTime createdAt;

        private long tagHitCount;


        public CommunitySearchDto(Long id,
                                  CommunityCategory category,
                                  String title,
                                  String content,
                                  LocalDateTime createdAt,
                                  Long authorId,
                                  String authorNickname,
                                  String authorImageUrl,
                                  Long tagHitCount,
                                  String tags) {

            this.id = id;
            this.category = category;
            this.title = title;
            this.content = (content != null && content.length() >= MAX_CONTENT_LENGTH) ?
                    content.substring(0, MAX_CONTENT_LENGTH) + "..." : content;
            this.tags = tags;
            this.authorId = authorId;
            this.authorNickname = authorNickname;
            this.authorImageUrl = authorImageUrl;
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
