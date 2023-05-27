package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.Code;
import much.api.entity.Position;
import much.api.entity.User;
import much.api.exception.InvalidValueException;
import much.api.exception.NotFoundException;
import much.api.exception.RequiredException;
import much.api.repository.PositionRepository;
import much.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static java.lang.String.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PositionRepository positionRepository;


    @Override
    @Transactional
    public User initUser(Long id, String positionIds, String positionClass) {

        // 사용자 확인
        final User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Code.USER_NOT_FOUND, format("사용자 [%s] 를 찾지 못함", id)));

        // 포지션코드 검증
        List<Integer> codes = null;
        List<Position> byCodeIn = null;
        try {
            codes = Arrays.stream(positionIds.split(","))
                    .map(Integer::parseInt)
                    .toList();

            byCodeIn = positionRepository.findByCodeIn(codes);
        } catch (Exception ignored) {}

        if (codes == null || byCodeIn == null || codes.size() <= 1 || codes.size() != byCodeIn.size()) {
            throw new InvalidValueException("positionIds");
        }

        // 휴대폰번호 인증여부 확인
        if (!StringUtils.hasText(foundUser.getPhoneNumber())) {
            throw new RequiredException(Code.PHONE_NUMBER_CERTIFICATION_REQUIRED, "번호 미인증으로 번호 미등록상태");
        }

        // 정보 변경
        foundUser.setPositionIds(positionIds);
        foundUser.setPositionClass(positionClass);

        return foundUser;
    }

}
