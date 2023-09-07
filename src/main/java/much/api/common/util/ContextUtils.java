package much.api.common.util;

import much.api.common.enums.RunMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static much.api.common.enums.RunMode.*;

@Component
public class ContextUtils {

    private static RunMode runMode;

    private static String host;


    public static boolean isLocalMode() {

        return runMode == LOCAL;
    }

    public static boolean isDevMode() {

        return runMode == DEV;
    }

    public static boolean isProdMode() {

        return runMode == PROD;
    }

    public static Long getUserId() {

        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public static RunMode getRunMode() {

        return runMode;
    }

    public static String getHost() {

        return host;
    }


    @Value("${context.runMode}")
    public void setRunMode(String runMode) {

        if (runMode == null) throw new IllegalArgumentException();
        ContextUtils.runMode = RunMode.valueOf(runMode);
    }


    @Value("${context.host}")
    public void setHost(String host) {

        if (host == null) throw new IllegalArgumentException();
        ContextUtils.host = host;
    }
}
