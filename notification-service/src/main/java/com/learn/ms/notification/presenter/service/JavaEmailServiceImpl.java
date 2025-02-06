package com.learn.ms.notification.presenter.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class JavaEmailServiceImpl implements IEmailService {

    @Value("${spring.mail.username}")
    private String mailUserName;

    @Value("${spring.mail.personal}")
    private String mailPersonal;

    private final JavaMailSender emailSender;

    @Override
    public Boolean sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailUserName);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            emailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void sendMessageUsingFreeMarkerTemplate(String to, String subject, String content) throws MessagingException, jakarta.mail.MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();
        message.setFrom(mailUserName);
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        helper.setFrom(mailUserName, mailPersonal);
        helper.setTo(to);
        helper.setText(content, true);
        helper.setSubject(subject);
        emailSender.send(message);
    }
}
