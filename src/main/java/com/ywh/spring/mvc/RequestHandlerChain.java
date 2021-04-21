package com.ywh.spring.mvc;

import com.ywh.spring.mvc.handler.Handler;
import com.ywh.spring.mvc.render.DefaultRender;
import com.ywh.spring.mvc.render.InternalErrorRender;
import com.ywh.spring.mvc.render.Render;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

/**
 * 请求处理链
 *
 * @author ywh
 * @since 4/20/2021
 */
@Data
@Slf4j
public class RequestHandlerChain {
    /**
     * Handler 迭代器
     *
     * {@link Handler}
     */
    private Iterator<Handler> handlerIt;

    /**
     *
     */
    private HttpServletRequest request;

    /**
     */
    private HttpServletResponse response;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求路径
     */
    private String requestPath;

    /**
     * 请求状态码
     */
    private int responseStatus;

    /**
     * 请求结果处理器
     */
    private Render render;

    /**
     *
     * @param handlerIt
     * @param request
     * @param response
     */
    public RequestHandlerChain(Iterator<Handler> handlerIt, HttpServletRequest request, HttpServletResponse response) {
        this.handlerIt = handlerIt;
        this.request = request;
        this.response = response;
        this.requestMethod = request.getMethod();
        this.requestPath = request.getPathInfo();
        this.responseStatus = HttpServletResponse.SC_OK;
    }

    /**
     * 执行请求链
     */
    public void doHandlerChain() {
        try {
            while (handlerIt.hasNext()) {
                if (!handlerIt.next().handle(this)) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("doHandlerChain error", e);
            render = new InternalErrorRender();
        }
    }

    /**
     * 执行处理器
     */
    public void doRender() {
        if (null == render) {
            render = new DefaultRender();
        }
        try {
            render.render(this);
        } catch (Exception e) {
            log.error("doRender", e);
            throw new RuntimeException(e);
        }
    }
}
