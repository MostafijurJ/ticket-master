package com.learn.ms.payment.common.logger;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.ms.payment.common.utils.JacksonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.validation.BindingResult;

import java.util.Objects;

public abstract class BaseLoggerAspect {

    @Pointcut("@within(com.learn.ms.payment.common.annotations.NoLogging)")
    public void noLogging() {
    }

    private final ObjectMapper objectMapper = JacksonUtil.objectMapper();
    private static final String BLANK = " - ";

    protected String serializeResponseToJson(ProceedingJoinPoint joinPoint, Object object) throws JsonProcessingException {

        String response = objectMapper.writeValueAsString(object);

        if (response == null) response = BLANK;

        return String.format("%s.%s Response: %s", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName(), response);
    }

    protected String formatRequestParameters(JoinPoint joinPoint) throws JsonProcessingException {

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = ms.getParameterNames();
        Object[] args = joinPoint.getArgs();

        String requestParameters = objectMapper.writeValueAsString(parameterNames);

        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (Object param : args) {

            if (param instanceof BindingResult) {
                index++;
                continue;
            }
            if (Objects.isNull(param)) {
                if (index > 0) sb.append(" | ");
                sb.append(" - ");
            } else {
                if (index > 0) sb.append(" | ");
                String jsonParam = objectMapper.writeValueAsString(param);
                sb.append(jsonParam);
            }
            index++;
        }

        return String.format("\nClass: %s \nMethod: %s \nArguments Name: %s \nArguments Value: %s\n", className, methodName, requestParameters, sb);
    }



}
