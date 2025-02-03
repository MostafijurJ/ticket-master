package com.learn.ms.kafka.producer;

import com.learn.ms.kafka.domain.EventWrapper;
import com.learn.ms.kafka.logger.KafkaServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CommonProducer {
    private final KafkaServiceLogger logger;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String topicName, EventWrapper<Object> eventWrapper) {
        kafkaTemplate.send(topicName, eventWrapper);
    }

    @Async
    public void sendMessageAsync(String topicName, EventWrapper<Object> eventWrapper) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, eventWrapper);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.trace("Sent message=[" + eventWrapper + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                logger.trace("Unable to send message=[" + eventWrapper + "] due to : " + ex.getMessage());
            }
        });
    }
}
