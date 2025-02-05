package com.learn.ms.notification.core.service;

import com.learn.ms.notification.core.domain.enums.NotificationType;
import com.learn.ms.notification.core.service.impl.EmailNotification;
import com.learn.ms.notification.core.service.impl.PushNotification;
import com.learn.ms.notification.core.service.impl.SmsNotification;

import java.util.ArrayList;
import java.util.List;

public class NotificationFactory {
    public static NotificationStrategy getNotificationStrategy(List<NotificationType> notificationTypes) {
        List<NotificationStrategy> strategies = new ArrayList<>();
        for (NotificationType type : notificationTypes) {
            switch (type) {
                case SMS -> strategies.add(new SmsNotification());
                case EMAIL -> strategies.add(new EmailNotification());
                case PUSH -> strategies.add(new PushNotification());
                default -> throw new IllegalArgumentException("Invalid notification type: " + type);
            }
        }
        if (strategies.size() == 1) {
            return strategies.get(0);
        }

        return new CompositeNotification(strategies);
    }
}
