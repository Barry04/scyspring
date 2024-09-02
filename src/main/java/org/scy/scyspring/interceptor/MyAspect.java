package org.scy.scyspring.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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


    /**
     * 对方法执行过程中抛出的异常进行拦截，并记录详细信息。
     * 包括方法名称、异常信息、入参等。
     *
     * @param joinPoint AOP切点
     * @param e         异常对象
     */
    @AfterThrowing(pointcut = "pointCut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        // 获取方法签名
        String methodName = joinPoint.getSignature().getName();

        // 获取入参列表
        Object[] args = joinPoint.getArgs();

        // 记录详细的异常信息
        log.error("{} error: {}, Args: {}",
                methodName,
                ExceptionUtils.getStackTrace(e),
                Arrays.toString(args)); // 打印入参信息
    }


}
