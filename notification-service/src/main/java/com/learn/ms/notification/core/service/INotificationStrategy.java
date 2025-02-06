package com.learn.ms.notification.core.service;

import com.learn.ms.notification.core.domain.model.TemplateData;

public interface INotificationStrategy {
    void sendNotification(TemplateData templateData);
}
