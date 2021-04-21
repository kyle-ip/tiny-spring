package com.ywh.spring.mvc.server;

/**
 *
 *
 * @author ywh
 * @since 4/20/2021
 */
public interface Server {
    /**
     * 启动服务器
     *
     * @throws Exception Exception
     */
    void startServer() throws Exception;

    /**
     * 停止服务器
     *
     * @throws Exception Exception
     */
    void stopServer() throws Exception;
}
