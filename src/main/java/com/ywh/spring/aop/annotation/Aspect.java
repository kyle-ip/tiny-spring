package com.ywh.spring.aop.annotation;

import java.lang.annotation.*;

/**
 * @author ywh
 * @since 31/03/2021
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    /**
     * 目标代理类的范围
     */
    Class<? extends Annotation> target();
}