package com.ncc.manage_emp.modal_example;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Data
//creates the bean instance for the lifecycle of a ServletContext.
// SIMILAR SINGLETON BUT SINGLETON SCOPE APP CONTEXT ONLY
// APPLICATION SCOPE SHARE MULTI APP BASED APPLICATION RUNNING IN THE SAME SERVLET CONTEXT
@Scope(
        value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSession {
    @PostConstruct
    public void postConstruct(){
        System.out.println("\t>>After initialization, the UserSession object will run this function");
    }

    @PreDestroy
    public void preDestroy(){
        System.out.println("\t>> Before destroy, the UserSession object will run this function");
    }

    private String username;
    private boolean isLoggedIn;

}
