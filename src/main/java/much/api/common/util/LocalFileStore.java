package much.api.common.util;

import much.api.common.enums.Code;
import much.api.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class LocalFileStore implements FileStore {

    private static final String ROOT_PATH = System.getProperty("user.home");
    private static final String FILE_DIR = ROOT_PATH + File.separator + "muchFiles";
    private static final String TEMPORARY_DIR = FILE_DIR + File.separator + "temp";

    private static final String PROJECT_DIR = FILE_DIR + File.separator + "project";

    @Value("${context.host}")
    private String host;


    @Override
    public String getTemporaryPath() {

        return TEMPORARY_DIR;
    }

    @Override
    public UploadResult temporaryUpload(MultipartFile file) {

        if (file.isEmpty()) {
            throw new BusinessException(Code.INVALID_VALUE_FOR, "업로드 파일 미존재");
        }

        final String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;

        final String storedFilename = UUID.randomUUID() + "." + extractExtension(originalFilename);

        File dir = new File(TEMPORARY_DIR);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new BusinessException(Code.FILE_UPLOAD_ERROR, "경로생성 실패");
        }

        try {
            file.transferTo(new File(TEMPORARY_DIR + File.separator + storedFilename));
        } catch (IOException e) {
            throw new BusinessException(Code.FILE_UPLOAD_ERROR, "파일 업로드 실패", e);
        }

        return new UploadResult(host + "/common/image/temp/" + storedFilename);
    }

    private String extractExtension(String originalFilename) {

        int position = originalFilename.lastIndexOf(".");
        return originalFilename.substring(position + 1);
    }

}
