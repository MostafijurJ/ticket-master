package com.learn.ms.notification.core.service.impl;

import com.learn.ms.notification.core.domain.model.TemplateData;
import com.learn.ms.notification.core.service.BaseService;
import com.learn.ms.notification.core.service.NotificationStrategy;

public class PushNotification extends BaseService implements NotificationStrategy {
    @Override
    public void sendNotification(TemplateData templateData) {
        logger.trace("Push Notification Sent to  :  {}", templateData.getToPhoneNumberList());
    }
}
