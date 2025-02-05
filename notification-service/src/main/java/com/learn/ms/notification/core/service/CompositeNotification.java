package com.learn.ms.notification.core.service;

import com.learn.ms.notification.core.domain.model.TemplateData;

import java.util.List;

public class CompositeNotification implements NotificationStrategy {
    private final List<NotificationStrategy> strategies;

    public CompositeNotification(List<NotificationStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public void sendNotification(TemplateData templateData) {
        for (NotificationStrategy strategy : strategies) {
            strategy.sendNotification(templateData);
        }
    }
}
