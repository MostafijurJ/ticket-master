package com.learn.ms.payment.presenter.producer;


import com.learn.ms.kafka.domain.EventWrapper;
import com.learn.ms.kafka.producer.CommonProducer;
import com.learn.ms.kafka.utils.KafkaUtil;
import com.learn.ms.payment.core.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class ProducerService extends BaseService {
    private final CommonProducer commonProducer;

    @Value(value = "${ms.kafka.topic.payment-post-processing-event-topic}")
    private String paymentPostProcessingEventTopic;


    public void producePostProcessingEvent(Object event, String correlationId) {
        commonProducer.sendMessageAsync(paymentPostProcessingEventTopic, getEventObject(correlationId, event));
    }


    private EventWrapper<Object> getEventObject(String correlationId, Object data) {
        return KafkaUtil.prepareKafkaObject(getRandomUUID(), correlationId, new Date(), data);
    }


}
