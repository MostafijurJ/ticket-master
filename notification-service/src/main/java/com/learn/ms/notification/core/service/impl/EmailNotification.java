package com.learn.ms.notification.core.service.impl;

import com.learn.ms.notification.core.domain.model.TemplateData;
import com.learn.ms.notification.core.service.BaseService;
import com.learn.ms.notification.core.service.NotificationStrategy;

public class EmailNotification extends BaseService implements NotificationStrategy {
    @Override
    public void sendNotification(TemplateData templateData) {
        logger.trace("Sending email to {} ", templateData.getToEmailList());
    }
}

