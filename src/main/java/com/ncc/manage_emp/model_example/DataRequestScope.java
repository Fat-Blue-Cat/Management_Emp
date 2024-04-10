package com.ncc.manage_emp.model_example;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DataRequestScope {
    private String name = "Request Scope";
    public DataRequestScope() {
        System.out.println("DataRequestScope Constructor Called");
    }

}