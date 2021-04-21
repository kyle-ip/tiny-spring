package com.ywh.spring.mvc.render;

import com.ywh.spring.mvc.RequestHandlerChain;

import javax.servlet.http.HttpServletResponse;

/**
 * 渲染 404
 *
 * @author ywh
 * @since 4/20/2021
 */
public class NotFoundRender implements Render {


    @Override
    public void render(RequestHandlerChain handlerChain) throws Exception {
        handlerChain.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
