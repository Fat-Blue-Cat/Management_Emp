package com.ncc.manage_emp.model_example;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Setter
@Getter
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DataSessionScope {
    private String name = "Session Scope";
    public DataSessionScope() {
        System.out.println("DataSessionScope Constructor Called at "+LocalDateTime.now());
    }

}