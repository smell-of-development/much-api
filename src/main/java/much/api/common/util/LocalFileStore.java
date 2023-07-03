package much.api.common.util;

import lombok.extern.slf4j.Slf4j;
import much.api.common.enums.ImageResizeType;
import much.api.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static much.api.common.enums.Code.*;

@Slf4j
@Component
public class LocalFileStore implements FileStore {

    private static final String ROOT_PATH = System.getProperty("user.home");
    private static final String FILE_DIR = ROOT_PATH + File.separator + "muchFiles";
    private static final String IMAGE_DIR = FILE_DIR + File.separator + "temp";

    private static final String ATTACHED_DIR = FILE_DIR + File.separator + "attached";


    @Override
    public String getImagePath() {

        return IMAGE_DIR;
    }

    @Override
    public String uploadImage(MultipartFile image, ImageResizeType resizeType) {

        if (image == null || image.isEmpty()) {
            throw new BusinessException(INVALID_VALUE_FOR, "업로드 파일 미존재");
        }
        checkImageType(image);

        final String originalFilename = image.getOriginalFilename();
        Objects.requireNonNull(image.getContentType());
        Objects.requireNonNull(originalFilename);

        final String extension = extractExtension(originalFilename);
        final String storedFilename = UUID.randomUUID() + "." + extension;

        File dir = new File(IMAGE_DIR);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new BusinessException(FILE_PROCESS_ERROR, "경로생성 실패");
        }

        try {
            boolean needsResizing = false;

            File dest = new File(IMAGE_DIR + File.separator + storedFilename);

            if (!resizeType.equals(ImageResizeType.NONE)) {
                needsResizing = true;

                BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
                int originWidth = bufferedImage.getWidth();
                int originHeight = bufferedImage.getHeight();

                int targetWidth = resizeType.getWidth();
                int targetHeight = resizeType.getHeight();

                if (originWidth <= targetWidth && originHeight <= targetHeight) {
                    needsResizing = false;
                } else {
                    // 리사이징 사이즈보다 작으면 그대로
                    targetWidth = Math.min(originWidth, targetWidth);
                    targetHeight = Math.min(originHeight, targetHeight);

                    Image resizedImage = bufferedImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

                    // 저장
                    BufferedImage newImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics graphics = newImage.getGraphics();
                    graphics.drawImage(resizedImage, 0, 0, null);
                    graphics.dispose();

                    ImageIO.write(newImage, extension, dest);
                }
            }

            if (!needsResizing) {
                image.transferTo(dest);
            }

        } catch (IOException e) {
            throw new BusinessException(FILE_PROCESS_ERROR, "파일 업로드 실패", e);
        }

        return "/temp/" + storedFilename;
    }

    private static void checkImageType(MultipartFile image) {

        final String contentType = image.getContentType();

        if (contentType == null || !contentType.startsWith("image")) {
            throw new BusinessException(NOT_IMAGE_FILE, String.format("이미지 형식이 아님[%s]", contentType));
        }
    }


    private String extractExtension(String originalFilename) {

        int position = originalFilename.lastIndexOf(".");
        return originalFilename.substring(position + 1);
    }

}
