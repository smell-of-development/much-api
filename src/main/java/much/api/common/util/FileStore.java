package much.api.common.util;

import lombok.Builder;
import lombok.Getter;
import much.api.common.enums.ImageResizeType;
import org.springframework.web.multipart.MultipartFile;

public interface FileStore {

    String getImagePath();

    UploadResult uploadImage(MultipartFile file, ImageResizeType resizeType);

    @Getter
    @Builder
    class UploadResult {

        private String extension;

        private String originalFilename;

        private String storedFilename;

        private String url;

    }

}
