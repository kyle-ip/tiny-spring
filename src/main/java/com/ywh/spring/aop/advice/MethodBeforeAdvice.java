package com.ywh.spring.aop.advice;

import java.lang.reflect.Method;

/**
 * 前置通知接口
 *
 * @author ywh
 * @since 31/03/2021
 */
public interface MethodBeforeAdvice extends Advice {

    /**
     * 前置方法
     *
     * @param clz    目标类
     * @param method 目标方法
     * @param args   目标方法参数
     * @throws Throwable Throwable
     */
    void before(Class<?> clz, Method method, Object[] args) throws Throwable;
}
