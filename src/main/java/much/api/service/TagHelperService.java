package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.MuchType;
import much.api.entity.Tag;
import much.api.entity.TagRelation;
import much.api.repository.TagRelationRepository;
import much.api.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagHelperService {

    private final TagRepository tagRepository;

    private final TagRelationRepository tagRelationRepository;

    @Transactional(propagation = MANDATORY)
    public void handleTagInformation(MuchType relationType,
                                     Long relationId,
                                     List<String> tags) {

        // 태그이름으로 정보조회
        List<Tag> existingTags = tagRepository.findAllByNameIn(tags);
        // toSet
        Set<String> tagInfos = existingTags.stream()
                .map(Tag::getName)
                .collect(toSet());
        // 태그정보가 없다면 등록
        List<Tag> saved = tagRepository.saveAll(
                tags.stream()
                        .filter(not(tagInfos::contains))
                        .map(Tag::ofName)
                        .toList());

        // 등록될 게시글의 태그 = 조회된 태그정보 + 없어서 등록한 태그정보
        existingTags.addAll(saved);
        // 기존 태그관계정보 삭제
        tagRelationRepository.deleteByTagNotIn(relationType, relationId, existingTags);

        // 새로운 태그정보 등록
        tagRelationRepository.saveAll(
                existingTags.stream()
                        .map(tag -> TagRelation.ofTypeAndId(relationType, relationId, tag))
                        .toList());
    }

}
