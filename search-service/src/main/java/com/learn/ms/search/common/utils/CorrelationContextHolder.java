package com.learn.ms.search.common.utils;

import org.slf4j.MDC;


public final class CorrelationContextHolder {
    private static final String CORRELATION_ID = "correlationId";

    private CorrelationContextHolder() {
    }

    public static void setCorrelationIdInContext(final String value) {
        MDC.put(CORRELATION_ID, value);
    }

    public static String getCorrelationIdFromContext() {
        return MDC.get(CORRELATION_ID);
    }

    public static void clear() {
        MDC.clear();
    }
}
