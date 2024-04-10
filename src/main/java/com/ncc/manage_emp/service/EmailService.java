package com.ncc.manage_emp.service;

import com.ncc.manage_emp.event.UpdateRoleEvent;
import org.thymeleaf.context.Context;

public interface EmailService  {

    void sendEmail(String to, String subject, String body);
    void sendEmailWithHtmlTemplate(String to, String subject, String templateName, Context context);

    void sendMailWhenUpdateRole(UpdateRoleEvent updateRoleEvent) throws InterruptedException;

}
