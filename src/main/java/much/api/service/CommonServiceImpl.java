package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.common.enums.Code;
import much.api.exception.DuplicatedException;
import much.api.dto.response.Envelope;
import much.api.dto.response.Positions;
import much.api.repository.PositionRepository;
import much.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonServiceImpl implements CommonService {

    private final PositionRepository positionRepository;

    private final UserRepository userRepository;


    @Override
    public Envelope<Positions> retrievePositions() {

        return Envelope.ok(
                new Positions(positionRepository.findByParentIsNull()
                        .stream()
                        .map(Positions.Position::of)
                        .collect(Collectors.toList())
                )
        );
    }


    @Override
    public void checkDuplicatedNickname(String nickname) {

        if (userRepository.existsByNickname(nickname)) {
            throw new DuplicatedException(Code.DUPLICATED_NICKNAME, String.format("닉네임 중복: [%s]", nickname));
        }

    }

}
