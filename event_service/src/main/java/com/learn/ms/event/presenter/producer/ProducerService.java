package com.learn.ms.event.presenter.producer;


import com.learn.ms.event.core.service.BaseService;
import com.learn.ms.kafka.domain.EventWrapper;
import com.learn.ms.kafka.producer.CommonProducer;
import com.learn.ms.kafka.utils.KafkaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class ProducerService extends BaseService {

    @Value(value = "${ms.kafka.topic.ticket-generation-topic}")
    private String ticketGenerationTopicName;


    private final CommonProducer commonProducer;


    public void produceTicketGenerationEvent(Object event, String correlationId) {
        commonProducer.sendMessageAsync(ticketGenerationTopicName, getEventObject(correlationId, event));
    }


    private EventWrapper<Object> getEventObject(String correlationId, Object data) {
        return KafkaUtil.prepareKafkaObject(getRandomUUID(), correlationId, new Date(), data);
    }


}
