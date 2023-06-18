package much.api.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

public interface FileStore {

    String getTemporaryPath();

    UploadResult temporaryUpload(MultipartFile file);

    @Getter
    @RequiredArgsConstructor
    class UploadResult {

        private final String url;

    }

}
