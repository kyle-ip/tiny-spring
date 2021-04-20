package com.ywh.spring;

import com.ywh.spring.aop.AOP;
import com.ywh.spring.core.BeanContainer;
import com.ywh.spring.ioc.IOC;
import com.ywh.spring.mvc.server.Server;
import com.ywh.spring.mvc.server.TomcatServer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * APP Starter
 *
 * @author ywh
 * @since 4/20/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class SpringApplication {

    /**
     * 全局配置
     */
    private static Configuration configuration = Configuration.builder().build();

    /**
     * 默认服务器
     */
    private static Server server;

    /**
     * 启动
     *
     * @param bootClass 启动服务器的类
     */
    public static void run(Class<?> bootClass) {
        run(Configuration.builder().bootClass(bootClass).build());
    }

    /**
     * 启动
     *
     * @param bootClass 启动服务器的类
     * @param port      服务器端口
     */
    public static void run(Class<?> bootClass, int port) {
        new SpringApplication().start(Configuration.builder().bootClass(bootClass).serverPort(port).build());
    }

    /**
     * 启动
     *
     * @param configuration 配置
     */
    public static void run(Configuration configuration) {
        new SpringApplication().start(configuration);
    }

    /**
     * 获取server
     *
     * @return 项目服务器
     */
    public static Server getServer() {
        return server;
    }

    /**
     * 获取全局配置
     *
     * @return 全局配置
     */
    public static Configuration getConfiguration() {
        return configuration;
    }

    /**
     * 初始化
     *
     * @param configuration 配置
     */
    private void start(Configuration configuration) {
        try {
            SpringApplication.configuration = configuration;
            String basePackage = configuration.getBootClass().getPackage().getName();
            BeanContainer.getInstance().loadBeans(basePackage);

            new AOP().doAOP();
            new IOC().doIOC();

            System.out.println("\n" +
                " ___________.__                 _________            .__                \n" +
                " \\__    ___/|__| ____ ___.__.  /   _____/____________|__| ____    ____  \n" +
                "   |    |   |  |/    <   |  |  \\_____  \\\\____ \\_  __ \\  |/    \\  / ___\\ \n" +
                "   |    |   |  |   |  \\___  |  /        \\  |_> >  | \\/  |   |  \\/ /_/  >\n" +
                "   |____|   |__|___|  / ____| /_______  /   __/|__|  |__|___|  /\\___  / \n" +
                "                    \\/\\/              \\/|__|                 \\//_____/\n" +
                " :: tiny-spring ::                                         (0.0.1-alpha)\n"
            );
            server = new TomcatServer(configuration);
            server.startServer();
        } catch (Exception e) {
            log.error("APP 启动失败", e);
        }
    }
}
