package much.api.service;

import lombok.RequiredArgsConstructor;
import much.api.dto.request.MuchRegistration;
import much.api.dto.response.Envelope;
import much.api.entity.Much;
import much.api.entity.WorkPosition;
import much.api.repository.MuchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MuchServiceImpl implements MuchService {

    private final MuchRepository muchRepository;

    @Override
    @Transactional
    public Envelope<Void> registerMuch(MuchRegistration registration) {

        final String skills = String.join(",", registration.getSkills());
        final String introduction = registration.getIntroduction();

        Much much = Much.builder()
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
                        .needs(w.getNeeds())
                        .build())
                .forEach(much::addWorkPosition);

        muchRepository.save(much);

        return Envelope.empty();
    }

}
