package much.api.common.aop;

import much.api.common.util.ValidationChecker;
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

    @Before("execution(* *(.., @much.api.common.aop.MuchValid (*), ..))")
    public void validate(JoinPoint joinPoint) {

        Object[] objectParameters = joinPoint.getArgs();

        try {
            for (Object object : objectParameters) {

                SelfCheck selfCheck = object.getClass().getAnnotation(SelfCheck.class);
                if (selfCheck != null) {
                    checkMyself(object, selfCheck);
                    continue;
                }

                Field[] fields = object.getClass().getDeclaredFields();
                for (Field field : fields) {
                    Check check = field.getAnnotation(Check.class);

                    if (check == null) {
                        continue;
                    }

                    checkWithValidationChecker(object, field, check);
                }

            }
        } catch (InvocationTargetException e) {
            throw (RuntimeException) e.getTargetException();
        } catch (Exception e) {
            throw new MuchException(INTERNAL_SERVER_ERROR, "Validation AOP 처리중 오류", e);
        }

    }


    private static void checkWithValidationChecker(Object object, Field field, Check check) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (check == null) return;

        Class<?> type = field.getType();
        Method method = ValidationChecker.class.getDeclaredMethod(check.value(), type);
        field.setAccessible(true);

        method.invoke(null, field.get(object));
    }


    private static void checkMyself(Object object, SelfCheck selfCheck) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (selfCheck == null) return;

        Method selfCheckMethod = object.getClass().getDeclaredMethod(selfCheck.value());
        selfCheckMethod.setAccessible(true);

        selfCheckMethod.invoke(object);
    }

}
