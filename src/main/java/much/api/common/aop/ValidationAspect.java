package much.api.common.aop;

import much.api.common.util.ValidationChecker;
import much.api.dto.Check;
import much.api.common.exception.MuchException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static much.api.common.enums.Code.INTERNAL_SERVER_ERROR;

@Aspect
@Component
public class ValidationAspect {

    @Before("execution(* *(.., @much.api.dto.MuchValid (*), ..))")
    public void validate(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {

            Field[] fields =  arg.getClass().getDeclaredFields();

            for (Field field : fields) {
                Check check = field.getAnnotation(Check.class);
                if (check == null) {
                    continue;
                }

                try {
                    Class<?> type = field.getType();
                    Method method = ValidationChecker.class.getDeclaredMethod(check.value(), type);
                    field.setAccessible(true);

                    method.invoke(null, field.get(arg));
                } catch (InvocationTargetException e) {
                    throw (RuntimeException) e.getTargetException();
                } catch (Exception e) {
                    throw new MuchException(INTERNAL_SERVER_ERROR, "Validation AOP 처리중 오류", e);
                }
            }

        }
    }

}
