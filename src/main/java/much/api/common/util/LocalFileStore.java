package much.api.common.util;

import lombok.extern.slf4j.Slf4j;
import much.api.common.enums.ImageResizeType;
import much.api.common.exception.FileProcessError;
import much.api.common.exception.MuchException;
import much.api.common.exception.NotImageFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static much.api.common.enums.Code.DEV_MESSAGE;

@Slf4j
@Component
public class LocalFileStore implements FileStore {

    private static final String FILE_DIR = System.getProperty("user.home") + File.separator + "muchFiles";
    private static final String IMAGE_DIR = FILE_DIR + File.separator + "image";

    private static final String ATTACHED_DIR = FILE_DIR + File.separator + "attached";


    @Override
    public String getImagePath() {

        return IMAGE_DIR;
    }


    /**
     * 이미지 파일을 업로드합니다.
     *
     * @param image      이미지 파일
     * @param resizeType 이미지 리사이징 타입
     * @return 업로드 결과 객체
     */
    @Override
    public UploadResult uploadImage(MultipartFile image, ImageResizeType resizeType) {

        if (image == null || image.isEmpty()) {
            throw new MuchException(DEV_MESSAGE, "업로드 파일 미존재");
        }
        checkImageType(image);

        final String originalFilename = image.getOriginalFilename();
        final String extension = extractExtension(originalFilename);
        final String storedFilename = UUID.randomUUID() + "." + extension;

        File dir = new File(IMAGE_DIR);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new FileProcessError("경로 생성 실패");
        }

        File dest = new File(IMAGE_DIR + File.separator + storedFilename);

        boolean resizeAndSaveOk = false;
        try {
            // 리사이징이 필요하다면 리사이징 후 저장
            if (!resizeType.equals(ImageResizeType.NONE)) {
                resizeAndSaveOk = resizeAndSave(image, resizeType, extension, dest);
            }

            // 원본 저장
            if (!resizeAndSaveOk) {
                image.transferTo(dest);
            }
        } catch (IOException e) {
            throw new FileProcessError("파일처리중 IO 예외 발생", e);
        }

        return UploadResult.builder()
                .extension(extension)
                .originalFilename(originalFilename)
                .storedFilename(storedFilename)
                .url(ContextUtils.getHost() + "/api/v1/common/image/" + storedFilename)
                .build();
    }


    private static boolean resizeAndSave(MultipartFile image,
                                         ImageResizeType resizeType,
                                         String extension,
                                         File dest) throws IOException {

        BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

        int originWidth = bufferedImage.getWidth();
        int originHeight = bufferedImage.getHeight();

        int targetWidth = resizeType.getWidth();
        int targetHeight = resizeType.getHeight();

        // 원본이 리사이징 목표 사이즈보다 작으면 리사이징 필요없음
        if (originWidth <= targetWidth && originHeight <= targetHeight) {
            return false;
        }

        // 리사이징
        targetWidth = Math.min(originWidth, targetWidth);
        targetHeight = Math.min(originHeight, targetHeight);
        Image resizedImage = bufferedImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

        // 저장
        BufferedImage newImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = newImage.getGraphics();
        graphics.drawImage(resizedImage, 0, 0, null);
        graphics.dispose();

        boolean result = ImageIO.write(newImage, extension, dest);
        if (!result) {
            throw new FileProcessError("이미지 리사이징 실패");
        }

        return true;
    }


    private static void checkImageType(MultipartFile image) {

        final String contentType = image.getContentType();

        if (contentType == null || !contentType.startsWith("image")) {
            throw new NotImageFile(contentType);
        }
    }


    private String extractExtension(String originalFilename) {

        if (originalFilename == null) return "";

        int position = originalFilename.lastIndexOf(".");
        return originalFilename.substring(position + 1);
    }

}
