package com.learn.ms.search.presenter.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.learn.ms.kafka.domain.EventWrapper;
import com.learn.ms.search.core.domain.event.EventDataForElastic;
import com.learn.ms.search.core.service.BaseService;
import com.learn.ms.search.core.service.IEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventDataConsumer extends BaseService {
    private final IEventService IEventService;

    @KafkaListener(topics = "${ms.kafka.topic.push-elastic-topic}", groupId = "${ms.kafka.consumer.group-id}")
    public void generateTickets(String event) {
        try {
            logger.trace("Event Data for Push to elastic received!! ->>>> " + event);
            EventWrapper<EventDataForElastic> eventWrapper = objectMapper.readValue(event, new TypeReference<>() {
            });
            IEventService.pushDataToElastic(eventWrapper.getData());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

}
