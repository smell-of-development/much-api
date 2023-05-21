package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.dto.response.Envelope;
import much.api.dto.response.PositionResponse;
import much.api.repository.PositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonServiceImpl implements CommonService {

    private final PositionRepository positionRepository;


    @Override
    public Envelope<PositionResponse> retrievePositions() {

        return Envelope.ok(
                new PositionResponse(positionRepository.findByParentIsNull()
                        .stream()
                        .map(PositionResponse.Position::of)
                        .collect(Collectors.toList())
                )
        );
    }

}
