package com.ncc.manage_emp.model_example;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class Customer {

    @Autowired
    private DataRequestScope dataRequestScope;

    @Autowired
    private DataSessionScope dataSessionScope;


}