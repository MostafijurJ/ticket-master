package com.learn.ms.payment.common.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ServiceLogger {

    private final Logger errorLogger;
    private final Logger traceLogger;

    public ServiceLogger() {
        this.errorLogger = LoggerFactory.getLogger("errorLogger");
        this.traceLogger = LoggerFactory.getLogger("traceLogger");
    }

    public void trace(String message) {
        traceLogger.trace(message);
    }
    public void trace(String message, Object... var2) {
        traceLogger.trace(message, var2);
    }

    public void error(String message, Exception ex) {
        errorLogger.error(message, ex);
    }

    public void error(String message) {
        errorLogger.error(message);
    }

}
