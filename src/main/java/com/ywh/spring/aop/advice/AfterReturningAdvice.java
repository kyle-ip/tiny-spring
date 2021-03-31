package com.ywh.spring.aop.advice;

import java.lang.reflect.Method;

/**
 * 返回通知接口
 *
 * @author ywh
 * @since 31/03/2021
 */
public interface AfterReturningAdvice extends Advice {

    /**
     * 返回后方法
     *
     * @param clazz         目标类
     * @param returnValue 方法结果
     * @param method      目标方法
     * @param args        目标方法参数
     * @throws Throwable Throwable
     */
    void afterReturning(Class<?> clazz, Object returnValue, Method method, Object[] args) throws Throwable;
}
