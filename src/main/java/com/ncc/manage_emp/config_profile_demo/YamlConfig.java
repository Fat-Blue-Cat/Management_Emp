package com.ncc.manage_emp.config_profile_demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
@Data
public class YamlConfig {
    private String name;
    private String environment;
    private boolean enabled;
    private List<String> servers = new ArrayList<>();


}