package com.learn.ms.notification.core.service;

import com.learn.ms.notification.core.domain.enums.NotificationType;
import com.learn.ms.notification.core.domain.model.TemplateData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService extends BaseService {
    private final NotificationFactory notificationFactory;

    public void sendNotification(TemplateData templateData) {
        logger.trace("Sending notification to : {}", templateData);
        List<NotificationType> types = templateData.getNotificationTypes();
        INotificationStrategy strategy = notificationFactory.getNotificationStrategy(types);
        strategy.sendNotification(templateData);
    }
}
