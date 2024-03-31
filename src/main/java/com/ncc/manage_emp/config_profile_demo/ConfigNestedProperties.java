package com.ncc.manage_emp.config_profile_demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "mail3")
@Configuration
@Data
public class ConfigNestedProperties {

    private String hostname;
    private int port;
    private String from;

    private List<String> defaultRecipients;
    private Map<String, String> additionalHeaders;
    private Credentials credentials;





    // standard getters and setters

}
