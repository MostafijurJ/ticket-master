package com.learn.ms.search_service.common.aspect;

import com.learn.ms.search_service.common.logger.CommonPerformanceLoggerAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServicePerformanceAspect extends CommonPerformanceLoggerAspect {

    @Pointcut("execution(public * com.learn.ms.search_service.data.repository..*.*(..))")
    public void repositoryPerformanceTrace() {
    }

    @Around("repositoryPerformanceTrace())")
    public Object accountServicePerformance(final ProceedingJoinPoint joinPoint) throws Throwable {
        return tracePerformance(joinPoint);
    }

}
