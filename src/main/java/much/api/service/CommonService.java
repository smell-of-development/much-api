package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.MuchType;
import much.api.common.exception.*;
import much.api.common.util.ContextUtils;
import much.api.entity.DevParameter;
import much.api.entity.User;
import much.api.entity.UserPick;
import much.api.repository.DevParameterRepository;
import much.api.repository.ProjectRepository;
import much.api.repository.UserPickRepository;
import much.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.lang.String.format;
import static much.api.common.enums.MuchType.PROJECT;
import static much.api.common.enums.MuchType.STUDY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonService {

    private final UserRepository userRepository;

    private final DevParameterRepository devParameterRepository;

    private final ProjectRepository projectRepository;

    private final UserPickRepository userPickRepository;


    public User getUserOrThrowException(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound(id));
    }


    public Optional<User> getUser(Long id) {

        return userRepository.findById(id);
    }


    public void checkDuplicatedLoginId(String loginId) {

        if (userRepository.existsByLoginId(loginId)) {
            throw new DuplicatedLoginID(loginId);
        }
    }


    public void checkDuplicatedNickname(String nickname) {

        if (userRepository.existsByNickname(nickname)) {
            throw new DuplicatedNickname(nickname);
        }

    }


    public void checkDuplicatedPhoneNumber(String phoneNumber) {

        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DuplicatedPhoneNumber(phoneNumber);
        }
    }


    public boolean isSmsPass() {

        if (ContextUtils.isDevMode()) {

            Optional<DevParameter> optional = devParameterRepository.findByName("SMS_PASS");
            return optional.isPresent() && optional.get().isUseYn();
        }

        return false;
    }


    @Transactional
    public Boolean addPickList(MuchType targetType, Long targetId) {

        if (!(targetType == PROJECT || targetType == STUDY)) {
            throw new PickProcessingFail("지원하는 타입이 아닙니다.");
        }

        // 사용자 확인
        Long userId = ContextUtils.getUserId();
        User user = getUserOrThrowException(userId);

        // target 존재 확인
        boolean exists = switch (targetType) {
            case PROJECT -> projectRepository.existsById(targetId);
            default -> false;
            // TODO 추후 STUDY 추가
        };
        if (!exists) {
            throw new PickProcessingFail(format("[%s | ID: %s] 찾을 수 없습니다.", targetType, targetId));
        }

        // 기존 찜 이력 확인. 없으면 새로 만들어 저장
        UserPick userPick = userPickRepository.findUserPick(user, targetType, targetId)
                .orElseGet(() -> userPickRepository.save(UserPick.builder()
                        .user(user)
                        .targetType(targetType)
                        .targetId(targetId)
                        .build()));

        // 찜 상태 변경
        return userPick.switchAvailable();
    }
}
