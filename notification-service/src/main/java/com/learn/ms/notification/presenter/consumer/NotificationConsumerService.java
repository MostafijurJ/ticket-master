package com.learn.ms.notification.presenter.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.learn.ms.kafka.domain.EventWrapper;
import com.learn.ms.notification.core.domain.model.TemplateData;
import com.learn.ms.notification.core.service.BaseService;
import com.learn.ms.notification.core.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumerService extends BaseService {

    private final NotificationService notificationService;

    @KafkaListener(topics = "${ms.kafka.topic.notification-topic}", groupId = "${ms.kafka.consumer.group-id}")
    public void consumeNotification(String event) {
        try {
            logger.trace("Notification Event Received: " + event);
            EventWrapper<TemplateData> eventWrapper = objectMapper.readValue(event, new TypeReference<>() {
            });
            notificationService.sendNotification(eventWrapper.getData());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error("Error while consuming notification event : " + event);
        }

    }
}
