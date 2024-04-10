package com.ncc.manage_emp.model_example;

import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Setter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Prototype {
    private String message;

    public void showMessage() {
        System.out.println("Message: " + message);
    }
}
