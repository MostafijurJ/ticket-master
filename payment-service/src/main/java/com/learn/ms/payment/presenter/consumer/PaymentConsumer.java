package com.learn.ms.payment.presenter.consumer;

import com.learn.ms.payment.presenter.service.PresenterBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentConsumer extends PresenterBaseService {
    @KafkaListener(topics = "${ms.kafka.topic.payment-topic-name}", groupId = "${ms.kafka.consumer.group-id}")
    public void generateTickets(String event) {
        try {
            logger.trace("Event Received for Payment: {}", event);

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

}
