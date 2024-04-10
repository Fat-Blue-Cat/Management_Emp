package com.ncc.manage_emp.model_custom_results.open_projections;

import org.springframework.beans.factory.annotation.Value;

public interface UserViewOpen {
    @Value("name:#{target.name + '  /email:' + target.email}")
    String getNameAndMail();
}
