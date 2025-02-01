package com.learn.ms.kafka.logger;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KafkaServiceLogger {
    private final Logger errorLogger;
    private final Logger traceLogger;

    public KafkaServiceLogger() {
        this.errorLogger = LoggerFactory.getLogger("errorLogger");
        this.traceLogger = LoggerFactory.getLogger("traceLogger");
    }

    public void trace(String message) {
        traceLogger.trace(message);
    }


    public void error(String message) {
        errorLogger.error(message);
    }

    public void error(String message, Object... args) {
        errorLogger.error(message, args);
    }

    public void error(String message, Exception ex) {
        errorLogger.error(message, ex);
    }
}
