package com.ncc.manage_emp.modal_example;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DataSessionScope {
    private String name = "Session Scope";

    public DataSessionScope() {
        System.out.println("DataSessionScope Constructor Called at "+LocalDateTime.now());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}