package com.learn.ms.notification.core.service;

import com.learn.ms.notification.core.domain.model.TemplateData;

public interface NotificationStrategy {
    void sendNotification(TemplateData templateData);
}
