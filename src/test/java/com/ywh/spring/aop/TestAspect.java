package com.ywh.spring.aop;

import com.ywh.spring.aop.advice.AroundAdvice;
import com.ywh.spring.aop.annotation.Aspect;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author ywh
 * @since 31/03/2021
 */
@Slf4j
@Aspect(pointcut = "within(com.ywh.spring.core.TestController)")
public class TestAspect implements AroundAdvice {

    /**
     *
     * @param clz
     * @param method 目标方法
     * @param args   目标方法参数
     */
    @Override
    public void before(Class<?> clz, Method method, Object[] args) {
        System.out.printf("Before TestAspect ----> class: %s, method: %s%n", clz.getName(), method.getName());
        
        // log.info("Before TestAspect ----> class: {}, method: {}", clz.getName(), method.getName());
    }

    /**
     *
     * @param clz
     * @param returnValue 方法结果
     * @param method      目标方法
     * @param args        目标方法参数
     */
    @Override
    public void afterReturning(Class<?> clz, Object returnValue, Method method, Object[] args) {
        System.out.printf("After TestAspect ----> class: %s, method: %s%n", clz.getName(), method.getName());
        // log.info("After TestAspect ----> class: {}, method: {}", clz, method.getName());
    }

    /**
     *
     * @param clz
     * @param method 目标方法
     * @param args   目标方法参数
     * @param e      抛出异常
     */
    @Override
    public void afterThrowing(Class<?> clz, Method method, Object[] args, Throwable e) {
        System.out.printf("Error TestAspect ----> class: %s, method: %s%n", clz.getName(), method.getName());
        // log.error("Error TestAspect ----> class: {}, method: {}, exception: {}", clz, method.getName(),e.getMessage());
    }
}
