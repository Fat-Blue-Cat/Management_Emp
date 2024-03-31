package com.ncc.manage_emp.config_profile_demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


//@ConfigurationProperties in Spring Boot
@Configuration
@ConfigurationProperties(prefix = "mail")
@Data

public class ConfigProperties {

    private String hostName;
    private int port;
    private String from;
}
