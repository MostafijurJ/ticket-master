package com.learn.ms.notification.core.service;

import com.learn.ms.notification.core.domain.model.TemplateData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeINotification implements INotificationStrategy {
    private final List<INotificationStrategy> strategies;

    @Override
    public void sendNotification(TemplateData templateData) {
        for (INotificationStrategy strategy : strategies) {
            strategy.sendNotification(templateData);
        }
    }
}
