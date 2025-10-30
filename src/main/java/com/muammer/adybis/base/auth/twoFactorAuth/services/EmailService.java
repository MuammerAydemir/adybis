package com.muammer.adybis.base.auth.twoFactorAuth.services;

import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public void sendCode(String toEmail, Map<String, Object> variables) throws MessagingException {
        String fromMail = "noreply@adybis.com";
        Context context = new Context();
        context.setVariables(variables);

        String htmlContent = templateEngine.process("mail-template", context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromMail);
        helper.setTo(toEmail);
        helper.setSubject("Verify Code");
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }

}
