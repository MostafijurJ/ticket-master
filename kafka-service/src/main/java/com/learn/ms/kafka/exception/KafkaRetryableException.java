package com.learn.ms.kafka.exception;

public class KafkaRetryableException extends RuntimeException implements Retryable {
    public KafkaRetryableException(Throwable cause) {
        super(cause);
    }
}
