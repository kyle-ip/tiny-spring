package com.ywh.spring.mvc.handler;

import com.ywh.spring.mvc.RequestHandlerChain;

/**
 * 请求执行器 Handler
 *
 * @author ywh
 * @since 4/20/2021
 */
public interface Handler {
    /**
     * 请求的执行器
     *
     * @param handlerChain {@link RequestHandlerChain}
     * @return 是否执行下一个
     * @throws Exception Exception
     */
    boolean handle(final RequestHandlerChain handlerChain) throws Exception;
}
