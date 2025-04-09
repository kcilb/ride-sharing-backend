package com.dev.test.ride_sharing_backend.AppConfigs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    public String keyPath;
    public String keyAlias;
    public String keyPassword;
    public String keyPassphrase;
}
