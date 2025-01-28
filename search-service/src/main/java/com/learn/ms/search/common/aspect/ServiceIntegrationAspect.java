package com.learn.ms.search.common.aspect;

import com.learn.ms.search.common.logger.CommonIntegrationLoggerAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceIntegrationAspect extends CommonIntegrationLoggerAspect {

    @Pointcut("execution(public * com.learn.ms.search.presenter.service..*.*(..))")
    public void accountIntegrationTrace() {
    }

    @Around("accountIntegrationTrace()")
    public Object traceAccountIntegration(ProceedingJoinPoint joinPoint) throws Throwable {
        return trace(joinPoint);
    }

}
