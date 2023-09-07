package much.api.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditorUtils {

    private static final String IMAGE_TAG_REGEX = "<img[^>]*src=[\"']?(?<imageUrl>[^>\"']+)[\"']?[^>]*>";

    private static final String HTML_TAG_REGEX = "<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>";

    private static final Pattern IMAGE_TAG_PATTERN = Pattern.compile(IMAGE_TAG_REGEX);


    public static String removeHtmlTags(String html) {

        return html
                .replaceAll(HTML_TAG_REGEX, "")
                .replaceAll("\\s+", " ")
                .trim();
    }


    public static List<String> extractImageFilenamesAtHtml(String html) {

        List<String> imageFilenames = new ArrayList<>();

        Matcher imageTagMatcher = IMAGE_TAG_PATTERN.matcher(html);
        while (imageTagMatcher.find()) {
            String imageUrl = imageTagMatcher.group("imageUrl");

            String imagePath = "image/";
            int index = imageUrl.lastIndexOf(imagePath);
            if (index == -1) {
                return imageFilenames;
            }

            String filename = imageUrl.substring(index + imagePath.length());
            imageFilenames.add(filename);
        }

        return imageFilenames;
    }

}
