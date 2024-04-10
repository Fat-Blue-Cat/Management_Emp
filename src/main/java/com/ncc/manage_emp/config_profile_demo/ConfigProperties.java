package com.ncc.manage_emp.config_profile_demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;


//@ConfigurationProperties in Spring Boot
//@Configuration
@ConfigurationPropertiesScan// 1 trong 2 cách để sử dụng @ConfigurationProperties
@ConfigurationProperties(prefix = "mail")
@Data

public class ConfigProperties {
    private String hostName;
    private int port;
    private String from;
}
