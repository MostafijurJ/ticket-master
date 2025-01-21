package com.learn.ms.search_service.common.aspect;

import com.learn.ms.search_service.common.logger.CommonTraceLoggerAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceTracingAspect extends CommonTraceLoggerAspect {

    @Pointcut("execution(public * com.learn.ms.search_service.presenter.rest.api.*.*(..))")
    public void controllerAspect() {
    }

    @Pointcut("execution(* com.learn.ms.search_service.common.aspect.ServiceTracingAspect.logService(..))")
    public void accountServiceTrace() {
    }



    @Around("accountServiceTrace() && !noLogging()")
    public Object logService(final ProceedingJoinPoint joinPoint) throws Throwable {
        return trace(joinPoint);
    }


    @Around("controllerAspect()")
    public Object logController(final ProceedingJoinPoint joinPoint) throws Throwable {
        return trace(joinPoint);
    }

}
