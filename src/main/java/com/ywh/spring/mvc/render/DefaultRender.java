package com.ywh.spring.mvc.render;


import com.ywh.spring.mvc.RequestHandlerChain;

/**
 * 渲染 200
 *
 * @author ywh
 * @since 4/20/2021
 */
public class DefaultRender implements Render {

    @Override
    public void render(RequestHandlerChain handlerChain) {
        int status = handlerChain.getResponseStatus();
        handlerChain.getResponse().setStatus(status);
    }
}
