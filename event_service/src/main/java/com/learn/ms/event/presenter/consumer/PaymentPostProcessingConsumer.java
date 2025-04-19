package com.learn.ms.event.presenter.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.learn.ms.event.core.domain.event.PostProcessingEvent;
import com.learn.ms.event.core.service.BaseService;
import com.learn.ms.event.core.service.BookingService;
import com.learn.ms.kafka.domain.EventWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentPostProcessingConsumer extends BaseService {
    private final BookingService bookingService;

    @KafkaListener(topics = "${ms.kafka.topic.payment-post-processing-event-topic}", groupId = "${ms.kafka.consumer.group-id}")
    public void generateTickets(String event) {
        try {
            logger.trace("Post Processing Event Received: " + event);
            EventWrapper<PostProcessingEvent> eventWrapper = objectMapper.readValue(event, new TypeReference<>() {
            });

            bookingService.processBooking(eventWrapper.getData());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

}
