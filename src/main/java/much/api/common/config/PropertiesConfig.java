package much.api.common.config;

import much.api.common.properties.JwtProperties;
import much.api.common.properties.OAuth2Properties;
import much.api.common.properties.SmsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {
        OAuth2Properties.class,
        JwtProperties.class,
        SmsProperties.class
})
public class PropertiesConfig {
}
