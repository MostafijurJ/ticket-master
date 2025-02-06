package com.learn.ms.notification.core.service.impl;

import com.learn.ms.notification.common.utils.NotificationUtils;
import com.learn.ms.notification.core.domain.enums.NotificationTemplateName;
import com.learn.ms.notification.core.domain.model.TemplateData;
import com.learn.ms.notification.core.service.BaseService;
import com.learn.ms.notification.core.service.INotificationStrategy;
import com.learn.ms.notification.presenter.service.IEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailINotification extends BaseService implements INotificationStrategy {
    private final IEmailService emailService;

    @Override
    public void sendNotification(TemplateData templateData) {
        Map<String, Object> additionalFields = templateData.getAdditionalFields();
        String emailSubject = additionalFields.get("emailSubject").toString();
        String templateName = getEmailNotificationTemplateName(templateData.getNotificationCode());
        Map<String, Object> templateValueMap = templateData.getAdditionalFields();
        String emailContent = NotificationUtils.prepareEmailNotificationContent(templateName, templateValueMap);
        try {
            for (String toEmail : templateData.getToEmailList()) {
                emailService.sendMessageUsingFreeMarkerTemplate(toEmail, emailSubject, emailContent);
                logger.trace("Email Notification Sent to :  {}", toEmail);
            }
        } catch (Exception e) {
            logger.error("Error while sending email notification: " + e.getMessage());
        }

    }


    private String getEmailNotificationTemplateName(Integer notificationCode) {
        return NotificationTemplateName.getEmailTemplateName(notificationCode);
    }
}

