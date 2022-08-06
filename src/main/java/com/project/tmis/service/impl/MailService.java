package com.project.tmis.service.impl;

import com.project.tmis.util.EmailConstraint;
import com.project.tmis.util.EmailStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

@Configuration
public class MailService {
    private static final Logger logger = LogManager.getLogger(MailService.class.getName());

    // private final JavaMailSender javaMailSender;
    private final EmailConstraint emailConstraint;

    public MailService(JavaMailSender javaMailSender, EmailConstraint emailConstraint) {
        this.emailConstraint = emailConstraint;
        //     this.javaMailSender = javaMailSender;
    }



    private EmailStatus sendMimeMail(String[] to, String subject, String body, Boolean isHtml, List<File> attachment) {
        try {
            Session session = emailConstraint.getSessionInstance();
            MimeMessage mimeMessage = new MimeMessage(session);
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(emailConstraint.getUserName());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, isHtml);
            if (attachment != null && attachment.size() > 0) {
                attachment.forEach(file -> {
                    try {
                        helper.addAttachment(file.getName(), file);
                    } catch (MessagingException e) {
                        logger.error("Error in attachment" + e.getMessage());
                    }
                });
            }
            Transport.send(mimeMessage);
            return new EmailStatus(to, subject, body).success();
        } catch (MessagingException e) {
            e.printStackTrace();
            return new EmailStatus(to, subject, body).error();
        }
    }





    public EmailStatus sendHtmlMail(String[] to, String subject, String body) {
        return sendMimeMail(to, subject, body, true, null);
    }

    public EmailStatus sendHtmlMail(String[] to, String subject, String body, List<File> attachment) {
        return sendMimeMail(to, subject, body, true, attachment);
    }

    public EmailStatus sendNonHtmlMail(String[] to, String subject, String body) {
        return sendMimeMail(to, subject, body, false, null);
    }

    public EmailStatus sendNonHtmlMail(String[] to, String subject, String body, List<File> attachment) {
        return sendMimeMail(to, subject, body, false, attachment);
    }
}
