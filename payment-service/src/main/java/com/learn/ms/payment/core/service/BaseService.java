package com.learn.ms.payment.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.ms.payment.common.logger.ServiceLogger;
import com.learn.ms.payment.common.utils.CorrelationContextHolder;
import com.learn.ms.payment.core.domain.enums.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class BaseService {
    protected ObjectMapper objectMapper;
    protected ServiceLogger logger;
    protected LocaleMessageService messageService;
    protected HttpServletRequest httpServletRequest;

    @Autowired
    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Autowired
    public void setLogger(ServiceLogger logger) {
        this.logger = logger;
    }

    @Lazy
    @Autowired
    public void setMessageService(LocaleMessageService messageService) {
        this.messageService = messageService;
    }

    @Lazy
    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected String getMessage(String key) {
        return messageService.getLocalMessage(key);
    }

    protected String getMessage(ResponseMessage key) {
        return messageService.getLocalMessage(key);
    }

    protected LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    protected  <T> T toObject(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    protected String getCorrelationId() {
        return CorrelationContextHolder.getCorrelationIdFromContext();
    }

    protected <T> void printTrace(T obj) {
        logger.trace(writeJsonString(obj));
    }

    protected <T> byte[] writeJsonByte(T obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(obj);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new byte[]{};
    }

    protected <T> String writeJsonString(T obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return StringUtils.EMPTY;
    }

    protected long getCurrentTimestamp() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        return instant.toEpochMilli();
    }

    protected String getRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }



}
