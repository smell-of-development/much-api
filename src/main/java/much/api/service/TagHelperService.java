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
import static much.api.entity.TagRelation.ofTypeAndId;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagHelperService {

    private final TagRepository tagRepository;

    private final TagRelationRepository tagRelationRepository;

    @Transactional(propagation = MANDATORY)
    public void handleTagRelation(MuchType relationType,
                                  Long relationId,
                                  Set<String> tags) {

        // 태그이름 저장
        Set<Tag> tagInfos = saveTagInfo(tags);

        // 기존의 태그 관계정보 조회
        Set<TagRelation> existingTagRelation = tagRelationRepository.findAllByRelation(relationType, relationId);

        Set<Tag> relationTag = existingTagRelation.stream()
                .map(TagRelation::getTag)
                .collect(toSet());

        // 저장될 태그정보에 포함되어있지 않은 태그관계 삭제
        List<TagRelation> toBeRemoved = existingTagRelation.stream()
                .filter(tr -> !tagInfos.contains(tr.getTag()))
                .toList();
        tagRelationRepository.deleteAll(toBeRemoved);

        // 새로운 태그 관계정보 등록
        List<TagRelation> toBeSaved = tagInfos.stream()
                .filter(not(relationTag::contains)) // 기존 태그관계에 해당하지 않았던 태그들
                .map(tag -> ofTypeAndId(relationType, relationId, tag))
                .toList();
        tagRelationRepository.saveAll(toBeSaved);
    }


    void deleteTagRelation(MuchType relationType,
                           Long relationId) {

        tagRelationRepository.deleteAllByRelation(relationType, relationId);
    }


    private Set<Tag> saveTagInfo(Set<String> tags) {

        // 태그이름으로 정보조회
        Set<Tag> existingTags = tagRepository.findAllByNameIn(tags);

        // extractNames
        Set<String> tagInfos = existingTags.stream()
                .map(Tag::getName)
                .collect(toSet());

        // 태그정보가 없다면 등록
        List<Tag> saved = tagRepository.saveAll(
                tags.stream()
                        .filter(not(tagInfos::contains))
                        .map(Tag::ofName)
                        .toList());

        existingTags.addAll(saved);
        return existingTags;
    }

}
