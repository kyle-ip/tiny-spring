package com.ywh.spring.aop;

import com.ywh.spring.aop.advice.Advice;
import com.ywh.spring.aop.annotation.Aspect;
import com.ywh.spring.aop.annotation.Order;
import com.ywh.spring.core.BeanContainer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AOP 启动类
 *
 * @author ywh
 * @since 31/03/2021
 */
public class AOP {

    /**
     * Bean容器
     */
    private final BeanContainer beanContainer;

    /**
     *
     */
    public AOP() {
        beanContainer = BeanContainer.getInstance();
    }

    /**
     *
     */
    public void doAOP() {
        // 创建所有的代理通知列表
        List<ProxyAdvisor> proxyList = beanContainer.getClassesBySuper(Advice.class)
            .stream()
            .filter(c -> c.isAnnotationPresent(Aspect.class))
            .map(this::createProxyAdvisor)
            .collect(Collectors.toList());

        beanContainer.getClassesBySuper(Advice.class)
            .stream()
            .filter(clz -> clz.isAnnotationPresent(Aspect.class))
            .map(this::createProxyAdvisor)
            .forEach(proxyAdvisor -> beanContainer.getClasses()
                .stream()
                .filter(c -> !Advice.class.isAssignableFrom(c))
                .filter(c -> !c.isAnnotationPresent(Aspect.class))
                .forEach(c -> {
                    List<ProxyAdvisor> matchProxies = createMatchProxies(proxyList, c);
                    if (proxyAdvisor.getPointcut().matches(c)) {
                        beanContainer.addBean(c, ProxyCreator.createProxy(c, matchProxies));
                    }
                }));
    }

    /**
     * @param proxyList
     * @param targetClass
     * @return
     */
    private List<ProxyAdvisor> createMatchProxies(List<ProxyAdvisor> proxyList, Class<?> targetClass) {
        Object targetBean = beanContainer.getBean(targetClass);
        return proxyList
            .stream()
            .filter(advisor -> advisor.getPointcut().matches(targetBean.getClass()))
            .sorted(Comparator.comparingInt(ProxyAdvisor::getOrder))
            .collect(Collectors.toList());
    }

    /**
     * 通过 Aspect 切面类创建代理通知类
     *
     * @param aspectClass
     * @return
     */
    private ProxyAdvisor createProxyAdvisor(Class<?> aspectClass) {
        int order = 0;
        if (aspectClass.isAnnotationPresent(Order.class)) {
            order = aspectClass.getAnnotation(Order.class).value();
        }
        String expression = aspectClass.getAnnotation(Aspect.class).pointcut();
        ProxyPointcut proxyPointcut = new ProxyPointcut();
        proxyPointcut.setExpression(expression);
        Advice advice = (Advice) beanContainer.getBean(aspectClass);
        return new ProxyAdvisor(advice, proxyPointcut, order);
    }
}
