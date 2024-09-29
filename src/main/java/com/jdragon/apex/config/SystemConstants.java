package com.jdragon.apex.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "system-constants")
public class SystemConstants {

    private boolean scheduler = true;

}
