package com.ncc.manage_emp.service;

import org.thymeleaf.context.Context;

public interface EmailService {

    void sendEmail(String to, String subject, String body);
    void sendEmailWithHtmlTemplate(String to, String subject, String templateName, Context context);
}
