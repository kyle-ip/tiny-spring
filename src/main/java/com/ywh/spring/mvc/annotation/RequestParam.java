package com.ywh.spring.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求参数
 *
 * @author ywh
 * @since 4/20/2021
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
    /**
     * 方法参数别名
     */
    String value() default "";

    /**
     * 是否必传
     */
    boolean required() default true;
}
