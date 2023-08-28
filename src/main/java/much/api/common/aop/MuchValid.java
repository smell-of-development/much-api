package much.api.common.aop;

import java.lang.annotation.*;

@Target({
        ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE,
})
@Retention(RetentionPolicy.RUNTIME)
public @interface MuchValid {
}
