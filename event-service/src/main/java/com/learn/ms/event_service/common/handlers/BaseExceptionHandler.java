package com.learn.ms.event_service.common.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.ms.event_service.core.domain.model.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public abstract class BaseExceptionHandler extends ResponseEntityExceptionHandler {
    protected static final Logger errorLogger = LoggerFactory.getLogger("errorLogger");
    protected static final ObjectMapper objectMapper = new ObjectMapper();

    protected String getMessageContent(String bodyContent) {
        try {
            final ApiResponse<?> apiResponse = objectMapper.readValue(bodyContent, new TypeReference<>() {
            });
            return apiResponse.getResponseMessage();
        } catch (Exception ex) {
            errorLogger.error(ex.getLocalizedMessage(), ex);
        }
        return StringUtils.isBlank(bodyContent) ? Strings.EMPTY : bodyContent;
    }

    protected <T> String serializeObject(T object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            errorLogger.error(e.getMessage(), e);
        }
        return StringUtils.EMPTY;
    }
}
