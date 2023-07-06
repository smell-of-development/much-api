package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.Code;
import much.api.common.enums.MuchType;
import much.api.common.util.ContextUtils;
import much.api.common.util.FileStore;
import much.api.dto.request.MuchRegistration;
import much.api.dto.response.Envelope;
import much.api.dto.response.MuchDetail;
import much.api.entity.Much;
import much.api.entity.User;
import much.api.entity.WorkPosition;
import much.api.exception.BusinessException;
import much.api.repository.MuchRepository;
import much.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MuchServiceImpl implements MuchService {

    private static final String IMAGE_TAG_REGEX = "<img[^>]*src=[\"']?(?<imageUrl>[^>\"']+)[\"']?[^>]*>";

    private static final Pattern IMAGE_TAG_PATTERN = Pattern.compile(IMAGE_TAG_REGEX);

    private final MuchRepository muchRepository;

    private final UserRepository userRepository;

    private final FileStore fileStore;


    @Override
    @Transactional
    public Envelope<Long> registerMuch(MuchRegistration registration, MuchType type) {

        final String skills = String.join(",", registration.getSkills());
        final String introduction = registration.getIntroduction();

        // TODO 업로드된 소개이미지/에디터이미지 폴더이동과 에디터 내 주소값 치환
//        Matcher imageTagMatcher = IMAGE_TAG_PATTERN.matcher(introduction);
//            while (imageTagMatcher.find()) {
//                String imageUrl = imageTagMatcher.group("imageUrl");
//                String[] hostAndFilename = imageUrl.split();
//
//                if (hostAndFilename.length != 2) continue;
//                String host = hostAndFilename[0];
//                String filename = hostAndFilename[1];
//
//                fileStore.moveFile(fileStore.getImagePath() + File.separator + filename,
//                        fileStore.getProjectPath() + File.separator + filename);
//
//                introduction = introduction.replace()
//            }

        long userId = ContextUtils.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(Code.USER_NOT_FOUND, String.format("사용자 [%s] 미존재", userId)));

        Much much = Much.builder()
                .writer(user)
                .type(type)
                .title(registration.getTitle())
                .imageUrl(registration.getImageUrl())
                .isOnline(registration.isOnline())
                .location(registration.getLocation())
                .deadline(registration.getDeadline())
                .startDate(registration.getStartDate())
                .endDate(registration.getEndDate())
                .schedule(registration.getSchedule())
                .target(registration.getTarget())
                .skills(skills)
                .introduction(introduction)
                .build();


        registration.getWork().stream()
                .map(w -> WorkPosition.builder()
                        .position(w.getPosition())
                        .current(0)
                        .needs(w.getNeeds())
                        .build())
                .forEach(much::addWorkPosition);

        muchRepository.save(much);

        return Envelope.ok(much.getId());
    }


    @Override
    public Envelope<MuchDetail> retrieveProject(Long id) {

        final Much project = muchRepository.findByIdAndType(id, MuchType.PROJECT)
                .orElseThrow(() -> new BusinessException(Code.PROJECT_NOT_FOUND, String.format("프로젝트 없음 [%s] ", id)));

        final User projectWriter = project.getWriter();

        // 스킬 잘라내기
        final String skills = project.getSkills();
        List<MuchDetail.SkillDetail> skillDetails = new ArrayList<>();

        Arrays.stream(skills.split(","))
                .forEach(s -> skillDetails.add(MuchDetail.SkillDetail.builder()
                        .name(s)
                        .imageUrl("") // TODO 추후 이미지 URL 설정
                        .build()));

        // 포지션 상세 생성 + 포지션 총 인원 합/포지션 전체 필요 인원 합구하기 + 포지션별 인원 설정
        final List<WorkPosition> workPositions = project.getWorkPositions();
        List<MuchDetail.WorkDetail> workDetails = new ArrayList<>();

        int currentSum = 0;
        int needsSum = 0;
        for (WorkPosition position : workPositions) {
            Integer current = position.getCurrent();
            Integer needs = position.getNeeds();

            currentSum += current;
            needsSum += needs;

            workDetails.add(MuchDetail.WorkDetail.builder()
                    .position(position.getPosition())
                    .current(current)
                    .needs(needs)
                    .build());
        }

        // 최종 조립 후 반환
        return Envelope.ok(
                MuchDetail.builder()
                        .id(project.getId())
                        .title(project.getTitle())
                        .writer(MuchDetail.WriterDetail.builder()
                                .id(projectWriter.getId())
                                .nickname(projectWriter.getNickname())
                                .pictureUrl(projectWriter.getPictureUrl())
                                .build())
                        .imageUrl(project.getImageUrl())
                        .isOnline(project.isOnline())
                        .location(project.getLocation())
                        .deadline(project.getDeadline())
                        .startDate(project.getStartDate())
                        .endDate(project.getEndDate())
                        .schedule(project.getSchedule())
                        .target(project.getTarget())
                        .currentTotal(currentSum)
                        .maximumPeople(needsSum)
                        .introduction(project.getIntroduction())
                        .skills(skillDetails)
                        .work(workDetails)
                        .build()
        );
    }

}
