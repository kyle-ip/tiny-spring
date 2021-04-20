package com.ywh.spring.mvc.render;


import com.ywh.spring.mvc.RequestHandlerChain;

/**
 * 渲染请求结果 interface
 *
 * @author ywh
 * @since 4/20/2021
 */
public interface Render {
    /**
     * 执行渲染
     *
     * @param handlerChain {@link RequestHandlerChain}
     * @throws Exception Exception
     */
    void render(RequestHandlerChain handlerChain) throws Exception;
}
