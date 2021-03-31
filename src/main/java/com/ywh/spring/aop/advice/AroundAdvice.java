package com.ywh.spring.aop.advice;

/**
 * 环绕通知接口
 *
 * @author ywh
 * @since 31/03/2021
 */
public interface AroundAdvice extends MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice {
}
