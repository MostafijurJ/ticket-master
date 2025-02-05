package com.learn.ms.notification.core.service.impl;

import com.learn.ms.notification.core.domain.model.TemplateData;
import com.learn.ms.notification.core.service.BaseService;
import com.learn.ms.notification.core.service.NotificationStrategy;

public class SmsNotification extends BaseService implements NotificationStrategy {
    @Override
    public void sendNotification(TemplateData templateData) {
        logger.trace("Sending SMS to :  {}", templateData.getToPhoneNumberList());
    }
}
