package com.ncc.manage_emp.modal_example;

import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SingleTon {
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public void showMessage() {
        System.out.println("Message: " + message);
    }
}
