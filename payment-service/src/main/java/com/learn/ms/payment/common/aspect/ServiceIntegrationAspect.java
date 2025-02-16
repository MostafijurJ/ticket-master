package com.learn.ms.payment.common.aspect;

import com.learn.ms.payment.common.logger.CommonIntegrationLoggerAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceIntegrationAspect extends CommonIntegrationLoggerAspect {

    @Pointcut("execution(public * com.learn.ms.payment.presenter.service..*.*(..))")
    public void serviceIntegrationTrace() {
    }

    @Around("serviceIntegrationTrace()")
    public Object traceIntegration(ProceedingJoinPoint joinPoint) throws Throwable {
        return trace(joinPoint);
    }

}
