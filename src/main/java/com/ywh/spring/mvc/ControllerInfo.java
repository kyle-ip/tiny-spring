package com.ywh.spring.mvc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Controller 相关信息
 *
 * @author ywh
 * @since 4/20/2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControllerInfo {
    /**
     * Controller 类
     */
    private Class<?> controllerClass;

    /**
     * 执行的方法
     */
    private Method invokeMethod;

    /**
     * 方法参数别名对应参数类型
     */
    private Map<String, Class<?>> methodParameter;
}
