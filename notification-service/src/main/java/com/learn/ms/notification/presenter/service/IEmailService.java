package com.learn.ms.notification.presenter.service;

import org.springframework.messaging.MessagingException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface IEmailService {
    Boolean sendSimpleMessage(String to, String subject, String text);
    void sendMessageUsingFreeMarkerTemplate(String to, String subject, String content) throws MessagingException, jakarta.mail.MessagingException, UnsupportedEncodingException;
}
