package much.api.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class EditorUtilsTest {

    @Test
    @DisplayName("HTML 태그가 제거된 문자열을 반환하는지 확인")
    void remove_html_tags() {
        String html = """
                <head>
                    <title>HTML !DOCTYPE declaration</title>
                </head>
                <body>

                    <p>이 문서는 HTML 문서입니다.</p>

                </body>
                </html>
                """;

        String removedHtmlTags = EditorUtils.removeHtmlTags(html);
        assertEquals("HTML !DOCTYPE declaration 이 문서는 HTML 문서입니다.", removedHtmlTags);
    }

}