package com.learn.ms.event.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.ms.event.common.logger.ServiceLogger;
import com.learn.ms.event.common.utils.CorrelationContextHolder;
import com.learn.ms.event.core.domain.enums.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class BaseService {
    protected ObjectMapper objectMapper;
    protected ServiceLogger logger;
    protected LocaleMessageService messageService;
    protected HttpServletRequest httpServletRequest;
    protected RedisTemplate<Object, Object> redisTemplate;


    protected static final String EVENT_TICKETS_AVAILABLE = "event:%s:tickets:available:%s";
    protected static final String EVENT_TICKETS_BOOKED = "event:%s:tickets:booked";
    protected static final String EVENT_TICKETS_KEY = "event:%s:tickets";
    protected static final String STATUS = "status";


    @Lazy
    @Autowired
    public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

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

    public String getMessage(String key) {
        return messageService.getLocalMessage(key);
    }

    public String getMessage(ResponseMessage key) {
        return messageService.getLocalMessage(key);
    }

    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public <T> T toObject(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public <T> List<T> toObjectList(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return Collections.emptyList();
    }


    public String getCorrelationId() {
        return CorrelationContextHolder.getCorrelationIdFromContext();
    }

    public <T> void printTrace(T obj) {
        logger.trace(writeJsonString(obj));
    }

    public <T> byte[] writeJsonByte(T obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(obj);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new byte[]{};
    }

    public <T> String writeJsonString(T obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return StringUtils.EMPTY;
    }

    public long getCurrentTimestamp() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        return instant.toEpochMilli();
    }

    protected Date getCurrentDate() {
        return new Date();
    }

    public String getRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }


}
