package com.ywh.spring.mvc;

import com.ywh.spring.mvc.handler.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * DispatcherServlet
 * 转发 HTTP 请求
 *
 * @author ywh
 * @since 4/20/2021
 */
@Slf4j
public class DispatcherServlet extends HttpServlet {

    /**
     * 请求执行链
     */
    private final List<Handler> HANDLER_CHAIN = new ArrayList<>();

    /**
     * 初始化Servlet
     */
    @Override
    public void init() {
        HANDLER_CHAIN.add(new PreRequestHandler());
        HANDLER_CHAIN.add(new SimpleUrlHandler(getServletContext()));
        HANDLER_CHAIN.add(new JspHandler(getServletContext()));
        HANDLER_CHAIN.add(new ControllerHandler());
    }

    /**
     * 执行请求
     *
     * @param req  {@link HttpServletRequest}
     * @param resp {@link HttpServletResponse}
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        RequestHandlerChain handlerChain = new RequestHandlerChain(HANDLER_CHAIN.iterator(), req, resp);
        handlerChain.doHandlerChain();
        handlerChain.doRender();
    }
}
