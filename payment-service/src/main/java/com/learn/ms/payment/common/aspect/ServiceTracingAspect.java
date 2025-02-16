package com.learn.ms.payment.common.aspect;

import com.learn.ms.payment.common.logger.CommonTraceLoggerAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceTracingAspect extends CommonTraceLoggerAspect {

    @Pointcut("execution(public * com.learn.ms.payment.presenter.rest.api.*.*(..))")
    public void controllerAspect() {
    }

    @Pointcut("execution(* com.learn.ms.payment.core.service..*.*(..)))")
    public void serviceTrace() {
    }

    @Around("serviceTrace() && !noLogging()")
    public Object logService(final ProceedingJoinPoint joinPoint) throws Throwable {
        return trace(joinPoint);
    }


    @Around("controllerAspect()")
    public Object logController(final ProceedingJoinPoint joinPoint) throws Throwable {
        return trace(joinPoint);
    }

}
