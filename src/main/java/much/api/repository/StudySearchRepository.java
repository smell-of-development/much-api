package much.api.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import much.api.dto.request.StudySearch;
import much.api.dto.response.PageElement;
import much.api.dto.response.StudySummary;
import much.api.entity.QStudy;
import much.api.entity.QTagRelation;
import much.api.entity.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.*;
import static much.api.common.enums.MuchType.STUDY;
import static much.api.entity.QStudy.study;
import static much.api.entity.QTagRelation.tagRelation;
import static much.api.entity.QUserPick.userPick;

@Slf4j
@Repository
public class StudySearchRepository extends QuerydslRepositorySupport {

    public StudySearchRepository() {
        super(Study.class);
    }

    public Page<StudySearchDto> searchStudies(StudySearch searchCondition, Long loginUserId) {

        PageRequest pageRequest = PageRequest.of(searchCondition.getPage(), searchCondition.getSize());

        if (searchCondition.getTags().isEmpty()) {
            return applyPagination(pageRequest,
                    qf -> select(Projections.constructor(StudySearchDto.class,
                                    study.id,
                                    study.title,
                                    study.imageUrl,
                                    study.online,
                                    study.address,
                                    study.deadline,
                                    study.meetingDays,
                                    study.needs,
                                    study.recruited,
                                    Expressions.asString(""),
                                    study.viewCount,
                                    JPAExpressions.select(userPick.available)
                                            .from(userPick)
                                            .where(
                                                    userPick.user.id.eq(loginUserId == null ? 0 : loginUserId),
                                                    userPick.targetType.eq(STUDY),
                                                    userPick.targetId.eq(study.id)
                                            ),
                                    Expressions.asNumber(0L)
                            )
                    )
                            .from(study)
                            .where(onlyRecruitingCondition(searchCondition.isOnlyRecruiting()))
                            .orderBy(study.id.desc())
                    ,

                    qf -> selectFrom(study)
                            .where(onlyRecruitingCondition(searchCondition.isOnlyRecruiting()))
            );
        }

        // 태그가 포함된 검색 (태그테이블 기준 JOIN)
        QTagRelation subTr = new QTagRelation("subTR");
        QTagRelation countTr = new QTagRelation("countTR");
        QStudy subS = new QStudy("subS");

        return applyPagination(pageRequest,
                qf -> select(Projections.constructor(StudySearchDto.class,
                        study.id,
                        JPAExpressions.select(subS.title).from(subS).where(subS.id.eq(study.id)),
                        JPAExpressions.select(subS.imageUrl).from(subS).where(subS.id.eq(study.id)),
                        JPAExpressions.select(subS.online).from(subS).where(subS.id.eq(study.id)),
                        JPAExpressions.select(subS.address).from(subS).where(subS.id.eq(study.id)),
                        JPAExpressions.select(subS.deadline).from(subS).where(subS.id.eq(study.id)),
                        JPAExpressions.select(subS.meetingDays).from(subS).where(subS.id.eq(study.id)),
                        JPAExpressions.select(subS.needs).from(subS).where(subS.id.eq(study.id)),
                        JPAExpressions.select(subS.recruited).from(subS).where(subS.id.eq(study.id)),
                        stringTemplate("GROUP_CONCAT({0})", tagRelation.tagName),
                        JPAExpressions.select(subS.viewCount).from(subS).where(subS.id.eq(study.id)),
                        JPAExpressions.select(userPick.available)
                                .from(userPick)
                                .where(
                                        userPick.user.id.eq(loginUserId == null ? 0 : loginUserId),
                                        userPick.targetType.eq(STUDY),
                                        userPick.targetId.eq(study.id)
                                ),
                        as(JPAExpressions
                                .select(countTr.count())
                                .from(countTr)
                                .where(countTr.relationType.eq(STUDY),
                                        countTr.relationId.eq(study.id),
                                        countTr.tagName.in(searchCondition.getTags())), "tagHitCount"))
                )
                        .from(tagRelation)
                        .join(study)
                        .on(tagRelation.relationType.eq(STUDY),
                                tagRelation.relationId.eq(study.id))
                        .where(onlyRecruitingCondition(searchCondition.isOnlyRecruiting()),
                                JPAExpressions
                                        .selectFrom(subTr)
                                        .where(subTr.relationType.eq(STUDY),
                                                subTr.relationId.eq(study.id),
                                                subTr.tagName.in(searchCondition.getTags())).exists())
                        .groupBy(study.id)
                        .orderBy(orderSpecifiers(searchCondition.isByRecent()))
                ,

                // 페이징을 위한 COUNT 쿼리
                qf -> select(study.id)
                        .from(tagRelation)
                        .join(study)
                        .on(tagRelation.relationType.eq(STUDY),
                                tagRelation.relationId.eq(study.id))
                        .where(onlyRecruitingCondition(searchCondition.isOnlyRecruiting()),
                                JPAExpressions
                                        .selectFrom(subTr)
                                        .where(subTr.relationType.eq(STUDY),
                                                subTr.relationId.eq(study.id),
                                                subTr.tagName.in(searchCondition.getTags())).exists())
                        .groupBy(study.id)
        );
    }

    @Nullable
    private static BooleanExpression onlyRecruitingCondition(boolean onlyRecruiting) {

        return onlyRecruiting ? study.deadline.goe(LocalDate.now()) : null;
    }


    private OrderSpecifier<?>[] orderSpecifiers(boolean byRecent) {

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (!byRecent) {
            orderSpecifiers.add(stringPath("tagHitCount").desc());
        }

        orderSpecifiers.add(study.id.desc());
        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }

    @Getter
    public static class StudySearchDto implements PageElement<StudySummary> {

        private static final int MAX_CONTENT_LENGTH = 50;

        private Long id;

        private String title;

        private String imageUrl;

        private Boolean online;

        private String address;

        private LocalDate deadline;

        private String meetingDays;

        private Integer needs;

        private Integer recruited;

        private String tags;

        private Long viewCount;

        private Boolean pick;

        private long tagHitCount;

        public StudySearchDto(Long id,
                              String title,
                              String imageUrl,
                              Boolean online,
                              String address,
                              LocalDate deadline,
                              String meetingDays,
                              Integer needs,
                              Integer recruited,
                              String tags,
                              Long viewCount,
                              Boolean pick,
                              long tagHitCount) {

            this.id = id;
            this.title = title;
            this.imageUrl = imageUrl;
            this.online = online;
            this.address = address;
            this.deadline = deadline;
            this.meetingDays = meetingDays;
            this.needs = needs;
            this.recruited = recruited;
            this.tags = tags;
            this.viewCount = viewCount;
            this.pick = pick;
            this.tagHitCount = tagHitCount;
        }

        @Override
        public StudySummary toResponseDto() {

            return StudySummary.builder()
                    .id(id)
                    .title(title)
                    .tags(List.of(tags.split(",")))
                    .online(online)
                    .address(address)
                    .timesPerWeek(meetingDays == null ? null : (long) meetingDays.split(",").length)
                    .deadlineDDay(ChronoUnit.DAYS.between(LocalDate.now(), deadline))
                    .pick(pick)
                    .imageUrl(imageUrl)
                    .viewCount(viewCount)
                    .needs(needs)
                    .recruited(recruited)
                    .build();
        }
    }
}
