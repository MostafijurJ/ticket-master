package com.learn.ms.event.presenter.producer;


import com.learn.ms.event.core.domain.event.PostProcessingEvent;
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

    private final CommonProducer commonProducer;

    @Value(value = "${ms.kafka.topic.ticket-generation-topic}")
    private String ticketGenerationTopicName;

    @Value(value = "${ms.kafka.topic.push-elastic-topic}")
    private String pushElasticTopicName;

    @Value(value = "${ms.kafka.topic.notification-topic}")
    private String notificationTopicName;

    @Value(value = "${ms.kafka.topic.payment-request-topic}")
    private String paymentInitiationTopicName;


    public void producePaymentInitiationEvent(Object event, String correlationId) {
        commonProducer.sendMessageAsync(paymentInitiationTopicName, getEventObject(correlationId, event));
    }

    public void produceTicketGenerationEvent(Object event, String correlationId) {
        commonProducer.sendMessageAsync(ticketGenerationTopicName, getEventObject(correlationId, event));
    }


    public void produceElasticSearchDate(Object event, String correlationId) {
        commonProducer.sendMessageAsync(pushElasticTopicName, getEventObject(correlationId, event));
    }

    public void produceNotificationEvent(Object event, String correlationId) {
        commonProducer.sendMessageAsync(notificationTopicName, getEventObject(correlationId, event));
    }

    public void publishToDeadLetterQueue(PostProcessingEvent postProcessingEvent) {

        //TODO: Implement the logic to publish the event to dead letter queue
    }


    private EventWrapper<Object> getEventObject(String correlationId, Object data) {
        return KafkaUtil.prepareKafkaObject(getRandomUUID(), correlationId, new Date(), data);
    }


}
