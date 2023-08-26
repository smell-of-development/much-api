package much.api.common.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {

    private final int expirationTimeInMinutes;

    private final int maxSendingCountPerDay;

    private final String certificationMessageFormat;

    private final String from;

    private final String host;

    private final String url;

    private final String pathVariableName;

    private final String serviceId;

    private final String accessKey;

    private final String secretKey;

    private final String accessKeyHeader;

    private final String timeStampHeader;

    private final String signatureHeader;

}
