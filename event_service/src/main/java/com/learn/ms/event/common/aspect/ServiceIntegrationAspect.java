package com.learn.ms.event.common.aspect;

import com.learn.ms.event.common.logger.CommonIntegrationLoggerAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceIntegrationAspect extends CommonIntegrationLoggerAspect {

    @Pointcut("execution(public * com.learn.ms.event_service.presenter.service..*.*(..))")
    public void accountIntegrationTrace() {
    }

    @Around("accountIntegrationTrace()")
    public Object traceAccountIntegration(ProceedingJoinPoint joinPoint) throws Throwable {
        return trace(joinPoint);
    }

}
