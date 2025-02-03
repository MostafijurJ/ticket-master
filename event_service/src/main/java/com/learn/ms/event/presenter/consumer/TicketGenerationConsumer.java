package com.learn.ms.event.presenter.consumer;


import com.fasterxml.jackson.core.type.TypeReference;
import com.learn.ms.event.core.domain.event.TicketGenerationEvent;
import com.learn.ms.event.core.service.BaseService;
import com.learn.ms.event.presenter.service.EventService;
import com.learn.ms.kafka.domain.EventWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketGenerationConsumer extends BaseService {
    private final EventService eventService;

    @KafkaListener(topics = "${ms.kafka.topic.ticket-generation-topic}", groupId = "${ms.kafka.consumer.group-id}")
    public void generateTickets(String event) {
        try {
            EventWrapper<TicketGenerationEvent> eventWrapper = objectMapper.readValue(event, new TypeReference<>() {
            });

            logger.trace("Ticket Generation Event Received: " + event);
            eventService.generateTicket(eventWrapper.getData());

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }


}
