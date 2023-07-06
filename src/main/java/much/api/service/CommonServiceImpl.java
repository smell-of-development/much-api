package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.common.util.ValidationChecker;
import much.api.exception.BusinessException;
import much.api.dto.response.Envelope;
import much.api.dto.response.Positions;
import much.api.repository.PositionRepository;
import much.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static much.api.common.enums.Code.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonServiceImpl implements CommonService {

    private final PositionRepository positionRepository;

    private final UserRepository userRepository;


    @Override
    public Envelope<Positions> retrievePositions() {

        return Envelope.ok(
                new Positions(
                        // 직군
                        positionRepository.findAllByCodeBetween(100, 999)
                                .stream()
                                .map(p -> new Positions.Position(p.getCode(), p.getName()))
                                .toList(),
                        // 경력
                        positionRepository.findAllByCodeBetween(1000, 9999)
                                .stream()
                                .map(p -> new Positions.Position(p.getCode(), p.getName()))
                                .toList()
                ));
    }


    @Override
    public void checkDuplicatedLoginId(String id) {

        if (!ValidationChecker.isValidLoginId(id)) {
            throw new BusinessException(INVALID_LOGIN_ID, "로그인 ID 형식에 맞지 않음");
        }

        if (userRepository.existsByLoginId(id)) {
            throw new BusinessException(DUPLICATED_LOGIN_ID, String.format("로그인 ID 중복: [%s]", id));
        }
    }


    @Override
    public void checkDuplicatedNickname(String nickname) {

        if (!ValidationChecker.isValidNickname(nickname)) {
            throw new BusinessException(INVALID_NICKNAME, "닉네임 형식에 맞지 않음");
        }

        if (userRepository.existsByNickname(nickname)) {
            throw new BusinessException(DUPLICATED_NICKNAME, String.format("닉네임 중복: [%s]", nickname));
        }

    }

}
