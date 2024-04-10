package com.ncc.manage_emp.service.impl;

import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.entity.WorkTime;
import com.ncc.manage_emp.event.UpdateRoleEvent;
import com.ncc.manage_emp.service.EmailService;
import jakarta.mail.SendFailedException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.mail.MessagingException;


@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }



    @Override
    public void sendEmailWithHtmlTemplate(String to, String subject, String templateName, Context context) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try {
            helper.setTo(to);
            helper.setSubject(subject);
            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MailSendException e) {

        } catch (MessagingException e) {
            System.out.println("Nothing!");

            // Xử lý ngoại lệ gửi mail
        }

    }

//    @Async
    @EventListener
    public void sendMailWhenUpdateRole(UpdateRoleEvent updateRoleEvent) throws InterruptedException {

        Users users = updateRoleEvent.getUsers();
        WorkTime workTime = updateRoleEvent.getWorkTime();

        Context context = new Context();
        context.setVariable("work_time", workTime);
        context.setVariable("user_data", users);
        System.out.println("MAI: GỬI MAIL CHECKIN CODE");
        sendEmailWithHtmlTemplate(users.getEmail(), "Grant account to Emp", "email-template", context);
        System.out.println("MAIL: THỰC THI KHI UPDATEROLE ACTIVE");

    }

}
