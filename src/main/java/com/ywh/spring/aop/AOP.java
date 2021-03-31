package com.ywh.spring.aop;

import com.ywh.spring.aop.advice.Advice;
import com.ywh.spring.aop.annotation.Aspect;
import com.ywh.spring.core.BeanContainer;

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
        beanContainer.getClassesBySuper(Advice.class)
            .stream()
            .filter(clazz -> clazz.isAnnotationPresent(Aspect.class))
            .forEach(clazz -> {
                final Advice advice = beanContainer.getBean(clazz);
                Aspect aspect = clazz.getAnnotation(Aspect.class);
                beanContainer.getClassesByAnnotation(aspect.target())
                    .stream()
                    .filter(target -> !Advice.class.isAssignableFrom(target))
                    .filter(target -> !target.isAnnotationPresent(Aspect.class))
                    .forEach(target -> beanContainer.addBean(target, ProxyCreator.createProxy(target, new ProxyAdvisor(advice))));
            });
    }
}
