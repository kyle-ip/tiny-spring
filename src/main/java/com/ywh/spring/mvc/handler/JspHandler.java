package com.ywh.spring.mvc.handler;


import com.ywh.spring.SpringApplication;
import com.ywh.spring.mvc.RequestHandlerChain;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

/**
 * jsp请求处理
 * 主要负责jsp资源请求
 *
 * @author ywh
 * @since 4/20/2021
 */
public class JspHandler implements Handler {

    /**
     * jsp请求的RequestDispatcher的名称
     */
    private static final String JSP_SERVLET = "jsp";

    /**
     * jsp的RequestDispatcher,处理jsp资源
     */
    private RequestDispatcher jspServlet;

    public JspHandler(ServletContext servletContext) {
        jspServlet = servletContext.getNamedDispatcher(JSP_SERVLET);
        if (null == jspServlet) {
            throw new RuntimeException("没有jsp Servlet");
        }
    }

    @Override
    public boolean handle(final RequestHandlerChain handlerChain) throws Exception {
        if (isPageView(handlerChain.getRequestPath())) {
            jspServlet.forward(handlerChain.getRequest(), handlerChain.getResponse());
            return false;
        }
        return true;
    }

    /**
     * 是否为jsp资源
     *
     * @param url 请求路径
     * @return 是否为jsp资源
     */
    private boolean isPageView(String url) {
        return url.startsWith(SpringApplication.getConfiguration().getViewPath());
    }
}
