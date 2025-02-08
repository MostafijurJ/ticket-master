package com.learn.ms.notification.core.service;

import com.learn.ms.notification.common.annotations.NoLogging;
import com.learn.ms.notification.core.domain.enums.NotificationType;
import com.learn.ms.notification.core.service.impl.EmailINotification;
import com.learn.ms.notification.core.service.impl.PushINotification;
import com.learn.ms.notification.core.service.impl.SmsINotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationFactory extends BaseService {
    private final EmailINotification emailNotification;
    private final SmsINotification smsNotification;
    private final PushINotification pushNotification;

    @NoLogging
    public INotificationStrategy getNotificationStrategy(List<NotificationType> notificationTypes) {
        List<INotificationStrategy> strategies = new ArrayList<>();
        for (NotificationType type : notificationTypes) {
            switch (type) {
                case SMS -> strategies.add(smsNotification);
                case EMAIL -> strategies.add(emailNotification);
                case PUSH -> strategies.add(pushNotification);
                default -> throw new IllegalArgumentException("Invalid notification type: " + type);
            }
        }
        return new CompositeINotification(strategies);
    }
}
