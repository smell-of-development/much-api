package much.api.common.util;

import much.api.common.enums.RunMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static much.api.common.enums.RunMode.*;

@Component
public class ContextUtils {

    private static String runMode;

    private static boolean smsPass;


    public static RunMode getRunMode() {

        return runMode.equals(PROD.name()) ? PROD : DEV;
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
    public void setRunMode(boolean smsPass) {

        if (runMode == null) throw new IllegalArgumentException();
        ContextUtils.smsPass = smsPass;
    }
}
