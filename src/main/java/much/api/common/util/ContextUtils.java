package much.api.common.util;

import much.api.common.enums.RunMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static much.api.common.enums.RunMode.*;

@Component
public class ContextUtils {

    private static String runMode;

    private static String host;

    private static boolean smsPass;


    public static long getUserId() {

        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public static RunMode getRunMode() {

        return runMode.equals(PROD.name()) ? PROD : DEV;
    }

    public static String getHost() {

        return host;
    }


    public static boolean isSmsPass() {

        return smsPass;
    }


    @Value("${context.runMode}")
    public void setRunMode(String runMode) {

        if (runMode == null) throw new IllegalArgumentException();
        ContextUtils.runMode = runMode;
    }

    @Value("${context.smsPass}")
    public void setRunMode(Boolean smsPass) {

        if (smsPass == null) throw new IllegalArgumentException();
        ContextUtils.smsPass = smsPass;
    }

    @Value("${context.host}")
    public void setHost(String host) {

        if (host == null) throw new IllegalArgumentException();
        ContextUtils.host = host;
    }
}
