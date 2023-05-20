package much.api.common.config;

import much.api.common.properties.JwtProperties;
import much.api.common.properties.OAuth2Properties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {
        OAuth2Properties.class,
        JwtProperties.class
})
public class PropertiesConfig {
}
