package com.learn.ms.kafka.utils;

import com.learn.ms.kafka.domain.EventWrapper;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class KafkaUtil {

    public static <FEEDBACK> EventWrapper<FEEDBACK> prepareKafkaObject(EventWrapper<?> request, FEEDBACK feedback) {
        return new EventWrapper<>(request.getEventId(), request.getCorrelationId(), request.getEventDate(), feedback);
    }

    public static <PAYLOAD> EventWrapper<PAYLOAD> prepareKafkaObject(String eventId, String correlationId, Date eventDate, PAYLOAD data) {
        return new EventWrapper<>(eventId, correlationId, eventDate, data);
    }

}
