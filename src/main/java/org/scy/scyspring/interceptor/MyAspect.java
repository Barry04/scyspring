package org.scy.scyspring.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 自定义切面类，用于拦截controller层的方法执行
 */
@Aspect
@Component
@Slf4j
public class MyAspect {

    /**
     * 定义切点，拦截org.scy.scyspring.controller包下的所有方法执行
     */
    @org.aspectj.lang.annotation.Pointcut("execution(* org.scy.scyspring.controller.*.*(..))")
    public void pointCut() {

    }


    /**
     * 方法执行后拦截，记录方法执行结果
     *
     * @param joinPoint 切入点对象，代表被拦截的方法
     * @param result    方法执行返回的结果
     */
    @AfterReturning(value = "pointCut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        log.debug("{} after rerun data: {}", joinPoint.getSignature().getName(), result);
    }

    /**
     * 环绕通知，对切点方法进行增强，可以控制方法是否执行以及执行时机等
     *
     * @param joinPoint 切入点对象，代表被拦截的方法
     * @return 被拦截方法的返回值
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        // 记录方法执行的起始时间
        long start = System.currentTimeMillis();
        try {
            // 执行被拦截的方法
            Object res = joinPoint.proceed();
            // 记录方法执行的结束时间，计算执行耗时
            long end = System.currentTimeMillis();
            log.debug("{} cost time: {} ms", joinPoint.getSignature().getName(), end - start);
            return res;
        } catch (Throwable e) {
            log.error("{} error: {}", joinPoint.getSignature().getName(), e.getMessage());
            // 如果方法执行出现异常，抛出运行时异常
            throw new RuntimeException(e);
        }

    }

    @AfterThrowing(value = "pointCut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("{} error: {}", joinPoint.getSignature().getName(), e.getMessage());
    }


}
