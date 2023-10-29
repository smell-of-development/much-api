package much.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import much.api.entity.User;

import java.time.LocalDateTime;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class StudyApplication {

    private Applicant applicant;

    private Long id;

    private String memo;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    @Schema(example = "yyyy.MM.dd HH:mm", type = "string")
    private LocalDateTime appliedAt;

    public static StudyApplication ofEntity(much.api.entity.StudyApplication application) {

        return StudyApplication.builder()
                .applicant(Applicant.ofUser(application.getMember()))
                .id(application.getId())
                .memo(application.getMemo())
                .appliedAt(application.getCreatedAt())
                .build();
    }

    @Getter
    private static class Applicant {

        private Long id;

        private String nickname;

        private String imageUrl;


        private Applicant(Long id, String nickname, String imageUrl) {
            this.id = id;
            this.nickname = nickname;
            this.imageUrl = imageUrl;
        }


        public static Applicant ofUser(User applicant) {

            return new Applicant(applicant.getId(), applicant.getNickname(), applicant.getImageUrl());
        }
    }

}
