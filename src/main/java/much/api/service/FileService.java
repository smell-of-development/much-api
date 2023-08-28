package much.api.service;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import much.api.common.enums.ImageResizeType;
import much.api.common.util.FileStore;
import much.api.entity.File;
import much.api.common.exception.FileProcessError;
import much.api.repository.FileRepository;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;

import static much.api.common.enums.FileType.IMAGE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final FileStore fileStore;

    private final FileRepository fileRepository;

    @Transactional
    public String uploadImage(MultipartFile image, ImageResizeType resizeType) {

        FileStore.UploadResult uploadResult = fileStore.uploadImage(image, resizeType);

        fileRepository.save(
                File.builder()
                        .type(IMAGE)
                        .imageResizeType(resizeType)
                        .extension(uploadResult.getExtension())
                        .originalFilename(uploadResult.getOriginalFilename())
                        .storedFilename(uploadResult.getStoredFilename())
                        .url(uploadResult.getUrl())
                        .build()
        );

        return uploadResult.getUrl();
    }


    public LocalImage getLocalImage(String storedFilename) {

        String fullPath = fileStore.getImagePath() + java.io.File.separator + storedFilename;

        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(Files.probeContentType(Paths.get(fullPath)));

            return LocalImage.builder()
                    .resource(new FileSystemResource(new java.io.File(fullPath)))
                    .contentType(mediaType.toString())
                    .build();
        } catch (Exception e) {
            throw new FileProcessError("로컬 이미지파일 처리중 예외", e);
        }
    }

    @Getter
    @Builder
    public static class LocalImage {

        private Resource resource;

        private String contentType;

    }

}
