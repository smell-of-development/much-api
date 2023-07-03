package much.api.common.util;

import much.api.common.enums.ImageResizeType;
import org.springframework.web.multipart.MultipartFile;

public interface FileStore {

    String getImagePath();

    String uploadImage(MultipartFile file, ImageResizeType resizeType);

}
