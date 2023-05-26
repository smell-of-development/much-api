package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.exception.DuplicatedNicknameException;
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
            throw new DuplicatedNicknameException();
        }

    }

}
