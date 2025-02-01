package com.learn.ms.kafka.interceptor;

import com.learn.ms.kafka.domain.EventWrapper;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

public class KafkaProducerInterceptor<T> implements ProducerInterceptor<String, EventWrapper<T>> {

    public static final String CURRENT_USER_CONTEXT_HEADER = "CurrentContext";
    private static final Logger errorLogger = LoggerFactory.getLogger("errorLogger");
    private static final Logger traceLogger = LoggerFactory.getLogger("traceLogger");


    @Override
    public ProducerRecord<String, EventWrapper<T>> onSend(ProducerRecord<String, EventWrapper<T>> producerRecord) {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .map(request -> request.getHeader(CURRENT_USER_CONTEXT_HEADER))
                .map(userContext -> createProducerRecordWithUserContext(producerRecord, userContext))
                .orElse(producerRecord);
    }

    private ProducerRecord<String, EventWrapper<T>> createProducerRecordWithUserContext(ProducerRecord<String, EventWrapper<T>> producerRecord, String userContext) {
        EventWrapper<T> eventWrapper = producerRecord.value();
        eventWrapper.setEventDate(new Date());
        eventWrapper.setUserContext(userContext);

        return new ProducerRecord<>(producerRecord.topic(), producerRecord.partition(), producerRecord.timestamp(), producerRecord.key(), eventWrapper);
    }


    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception exception) {
        if (exception == null) {
            traceLogger.trace("Acknowledgement received for record at offset: {}, partition: {}, timestamp: {}", recordMetadata.offset(), recordMetadata.partition(), System.currentTimeMillis());
        } else {
            errorLogger.error("Error while producing record to topic {}", recordMetadata.topic(), exception);
        }
    }

    @Override
    public void close() {
        // No operation necessary on close
    }

    @Override
    public void configure(Map<String, ?> map) {
        // No operation necessary on configure
    }
}
