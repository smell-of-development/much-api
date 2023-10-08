package much.api.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import much.api.dto.request.ProjectSearch;
import much.api.dto.response.PageElement;
import much.api.dto.response.ProjectSummary;
import much.api.entity.Project;
import much.api.entity.QProject;
import much.api.entity.QTagRelation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.*;
import static much.api.common.enums.MuchType.PROJECT;
import static much.api.entity.QProject.project;
import static much.api.entity.QTagRelation.tagRelation;
import static much.api.entity.QUserPick.userPick;

@Slf4j
@Repository
public class ProjectSearchRepository extends QuerydslRepositorySupport {

    public ProjectSearchRepository() {
        super(Project.class);
    }

    public Page<ProjectSearchDto> searchProjects(ProjectSearch searchCondition, Long loginUserId) {

        PageRequest pageRequest = PageRequest.of(searchCondition.getPage(), searchCondition.getSize());

        if (searchCondition.getTags().isEmpty()) {
            return applyPagination(pageRequest,
                    qf -> select(Projections.constructor(ProjectSearchDto.class,
                                    project.id,
                                    project.title,
                                    project.imageUrl,
                                    project.online,
                                    project.address,
                                    project.deadline,
                                    project.meetingDays,
                                    JPAExpressions
                                            .select(stringTemplate("GROUP_CONCAT({0})", tagRelation.tagName))
                                            .from(tagRelation)
                                            .where(tagRelation.relationType.eq(PROJECT),
                                                    tagRelation.relationId.eq(project.id)),
                                    project.viewCount,
                                    JPAExpressions.select(userPick.available)
                                            .from(userPick)
                                            .where(
                                                    userPick.user.id.eq(loginUserId),
                                                    userPick.targetType.eq(PROJECT),
                                                    userPick.targetId.eq(project.id)
                                            ),
                                    Expressions.asNumber(0L)
                            )
                    )
                            .from(project)
                            .where(onlyRecruitingCondition(searchCondition.isOnlyRecruiting()))
                            .orderBy(project.id.desc())
                    ,

                    qf -> selectFrom(project)
                            .where(onlyRecruitingCondition(searchCondition.isOnlyRecruiting()))
            );
        }

        // 태그가 포함된 검색 (태그테이블 기준 JOIN)
        QTagRelation subTr = new QTagRelation("subTR");
        QTagRelation countTr = new QTagRelation("countTR");
        QProject subP = new QProject("subC");

        return applyPagination(pageRequest,
                qf -> select(Projections.constructor(ProjectSearchDto.class,
                        project.id,
                        JPAExpressions.select(subP.title).from(subP).where(subP.id.eq(project.id)),
                        JPAExpressions.select(subP.imageUrl).from(subP).where(subP.id.eq(project.id)),
                        JPAExpressions.select(subP.online).from(subP).where(subP.id.eq(project.id)),
                        JPAExpressions.select(subP.address).from(subP).where(subP.id.eq(project.id)),
                        JPAExpressions.select(subP.deadline).from(subP).where(subP.id.eq(project.id)),
                        JPAExpressions.select(subP.meetingDays).from(subP).where(subP.id.eq(project.id)),
                        stringTemplate("GROUP_CONCAT({0})", tagRelation.tagName),
                        JPAExpressions.select(subP.viewCount).from(subP).where(subP.id.eq(project.id)),
                        JPAExpressions.select(userPick.available)
                                .from(userPick)
                                .where(
                                        userPick.user.id.eq(loginUserId),
                                        userPick.targetType.eq(PROJECT),
                                        userPick.targetId.eq(project.id)
                                ),
                        as(JPAExpressions
                                .select(countTr.count())
                                .from(countTr)
                                .where(countTr.relationType.eq(PROJECT),
                                        countTr.relationId.eq(project.id),
                                        countTr.tagName.in(searchCondition.getTags())), "tagHitCount"))
                )
                        .from(tagRelation)
                        .join(project)
                        .on(tagRelation.relationType.eq(PROJECT),
                                tagRelation.relationId.eq(project.id))
                        .where(onlyRecruitingCondition(searchCondition.isOnlyRecruiting()),
                                JPAExpressions
                                        .selectFrom(subTr)
                                        .where(subTr.relationType.eq(PROJECT),
                                                subTr.relationId.eq(project.id),
                                                subTr.tagName.in(searchCondition.getTags())).exists())
                        .groupBy(project.id)
                        .orderBy(orderSpecifiers(searchCondition.isByRecent()))
                ,

                // 페이징을 위한 COUNT 쿼리
                qf -> select(project.id)
                        .from(tagRelation)
                        .join(project)
                        .on(tagRelation.relationType.eq(PROJECT),
                                tagRelation.relationId.eq(project.id))
                        .where(onlyRecruitingCondition(searchCondition.isOnlyRecruiting()),
                                JPAExpressions
                                        .selectFrom(subTr)
                                        .where(subTr.relationType.eq(PROJECT),
                                                subTr.relationId.eq(project.id),
                                                subTr.tagName.in(searchCondition.getTags())).exists())
                        .groupBy(project.id)
        );
    }

    @Nullable
    private static BooleanExpression onlyRecruitingCondition(boolean onlyRecruiting) {

        return onlyRecruiting ? project.deadline.goe(LocalDate.now()) : null;
    }


    private OrderSpecifier<?>[] orderSpecifiers(boolean byRecent) {

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (!byRecent) {
            orderSpecifiers.add(stringPath("tagHitCount").desc());
        }

        orderSpecifiers.add(project.id.desc());
        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }

    @Getter
    public static class ProjectSearchDto implements PageElement<ProjectSummary> {

        private static final int MAX_CONTENT_LENGTH = 50;

        private Long id;

        private String title;

        private String imageUrl;

        private Boolean online;

        private String address;

        private LocalDate deadline;

        private String meetingDays;

        private String tags;

        private Long viewCount;

        private Boolean pick;

        private long tagHitCount;

        public ProjectSearchDto(Long id,
                                String title,
                                String imageUrl,
                                Boolean online,
                                String address,
                                LocalDate deadline,
                                String meetingDays,
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
            this.tags = tags;
            this.viewCount = viewCount;
            this.pick = pick;
            this.tagHitCount = tagHitCount;
        }

        @Override
        public ProjectSummary toResponseDto() {

            return ProjectSummary.builder()
                    .id(id)
                    .title(title)
                    .tags(List.of(tags.split(",")))
                    .online(online)
                    .address(address)
                    .timesPerWeek(meetingDays == null ? null : (long) meetingDays.split(",").length)
                    .deadlineDDay(ChronoUnit.DAYS.between(LocalDate.now(), deadline))
                    .pick(pick != null && pick)
                    .imageUrl(imageUrl)
                    .viewCount(viewCount)
                    .build();
        }
    }
}
