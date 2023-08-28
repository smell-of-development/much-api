package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.MuchType;
import much.api.entity.Tag;
import much.api.entity.TagRelation;
import much.api.repository.TagRelationRepository;
import much.api.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagHelperService {

    private final TagRepository tagRepository;

    private final TagRelationRepository tagRelationRepository;

    @Transactional
    public void saveTagInformation(MuchType relationType,
                                   Long relationId,
                                   List<String> tags) {

        // 기존 태그관계정보 삭제
        tagRelationRepository.deleteExistingRelation(relationType, relationId);

        // 이미 등록된 태그정보 조회
        List<Tag> existingTags = tagRepository.findAllByNameIn(tags);

        // Set<태그이름> 형태로 변경
        Set<String> registeredTag = existingTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        // 미등록된 태그 추출하여 저장
        List<Tag> nonexistentTag = tagRepository.saveAll(
                tags.stream()
                        .filter(not(registeredTag::contains))
                        .map(Tag::ofName)
                        .toList());

        // 글과 태그관계 저장
        Stream.of(existingTags, nonexistentTag)
                .flatMap(Collection::stream)
                .forEach(tag ->
                        tagRelationRepository.save(
                                TagRelation.ofTypeAndId(relationType, relationId, tag)
                        )
                );
    }

}
