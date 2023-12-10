package com.vehicle.manager.vehicle.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "folder")
@Getter
@Setter
public class FolderPropertiesConfig {
    private String product;
}
