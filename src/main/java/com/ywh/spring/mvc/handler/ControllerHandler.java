package com.ywh.spring.mvc.handler;

import com.ywh.spring.core.BeanContainer;
import com.ywh.spring.mvc.ControllerInfo;
import com.ywh.spring.mvc.PathInfo;
import com.ywh.spring.mvc.RequestHandlerChain;
import com.ywh.spring.mvc.annotation.RequestMapping;
import com.ywh.spring.mvc.annotation.RequestParam;
import com.ywh.spring.mvc.annotation.ResponseBody;
import com.ywh.spring.mvc.render.JsonRender;
import com.ywh.spring.mvc.render.NotFoundRender;
import com.ywh.spring.mvc.render.Render;
import com.ywh.spring.mvc.render.ViewRender;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.ywh.spring.util.CommonUtil.EMPTY;
import static com.ywh.spring.util.CommonUtil.SLASH;

/**
 * REST 请求处理
 *
 * @author ywh
 * @since 4/20/2021
 */
@Slf4j
public class ControllerHandler implements Handler {
    /**
     * 请求信息与 controller 的映射
     */
    private Map<PathInfo, ControllerInfo> pathControllerMap = new ConcurrentHashMap<>();

    /**
     * Bean 容器
     */
    private BeanContainer beanContainer;

    /**
     *
     */
    public ControllerHandler() {
        beanContainer = BeanContainer.getInstance();
        Set<Class<?>> mappingSet = beanContainer.getClassesByAnnotation(RequestMapping.class);
        this.initPathControllerMap(mappingSet);
    }

    /**
     *
     * @param handlerChain {@link RequestHandlerChain}
     * @return
     * @throws Exception
     */
    @Override
    public boolean handle(final RequestHandlerChain handlerChain) throws Exception {
        String method = handlerChain.getRequestMethod();
        String path = handlerChain.getRequestPath();
        ControllerInfo controllerInfo = pathControllerMap.get(new PathInfo(method, path));
        if (null == controllerInfo) {
            handlerChain.setRender(new NotFoundRender());
            return false;
        }
        Object result = invokeController(controllerInfo, handlerChain.getRequest());
        setRender(result, controllerInfo, handlerChain);
        return true;
    }

    /**
     * 执行controller方法
     *
     * @param controllerInfo controller信息
     * @param request        HttpServletRequest
     * @return 执行方法结果
     */
    private Object invokeController(ControllerInfo controllerInfo, HttpServletRequest request) {
        Map<String, String> requestParams = getRequestParams(request);
        List<Object> methodParams = instantiateMethodArgs(controllerInfo.getMethodParameter(), requestParams);

        Object controller = beanContainer.getBean(controllerInfo.getControllerClass());
        Method invokeMethod = controllerInfo.getInvokeMethod();
        invokeMethod.setAccessible(true);
        Object result;
        try {
            if (methodParams.size() == 0) {
                result = invokeMethod.invoke(controller);
            } else {
                result = invokeMethod.invoke(controller, methodParams.toArray());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 设置请求结果执行器
     *
     * @param result         controller执行后的结果
     * @param controllerInfo controller信息
     * @param handlerChain   RequestHandlerChain
     */
    private void setRender(Object result, ControllerInfo controllerInfo, RequestHandlerChain handlerChain) {
        if (null == result) {
            return;
        }
        Render render;
        boolean isJson = controllerInfo.getInvokeMethod().isAnnotationPresent(ResponseBody.class);
        if (isJson) {
            render = new JsonRender(result);
        } else {
            render = new ViewRender(result);
        }
        handlerChain.setRender(render);
    }

    /**
     *
     * @param mappingSet 被{@link RequestMapping}注解的类集合
     */
    private void initPathControllerMap(Set<Class<?>> mappingSet) {
        mappingSet.forEach(this::addPathController);
    }

    /**
     *
     * @param clz 被{@link RequestMapping}注解的类
     */
    private void addPathController(Class<?> clz) {
        RequestMapping requestMapping = clz.getAnnotation(RequestMapping.class);
        String basePath = requestMapping.value();
        if (!basePath.startsWith(SLASH)) {
            basePath = SLASH + basePath;
        }
        for (Method method : clz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping methodRequest = method.getAnnotation(RequestMapping.class);
                String methodPath = methodRequest.value();
                if (!methodPath.startsWith(SLASH) && !SLASH.equals(basePath)) {
                    methodPath = SLASH + methodPath;
                }
                String url = basePath + methodPath;
                Map<String, Class<?>> methodParams = this.getMethodParams(method);
                String httpMethod = String.valueOf(methodRequest.method());
                PathInfo pathInfo = new PathInfo(httpMethod, url);
                if (pathControllerMap.containsKey(pathInfo)) {
                    log.warn("url:{} 重复注册", pathInfo.getHttpPath());
                }
                ControllerInfo controllerInfo = new ControllerInfo(clz, method, methodParams);
                this.pathControllerMap.put(pathInfo, controllerInfo);
                log.info("mapped:[{},method=[{}]] controller:[{}@{}]",
                    pathInfo.getHttpPath(), pathInfo.getHttpMethod(),
                    controllerInfo.getControllerClass().getName(), controllerInfo.getInvokeMethod().getName());
            }
        }
    }

    /**
     * 获取执行方法的参数
     *
     * @param method 执行的方法
     * @return 参数别名对应的参数类型
     */
    private Map<String, Class<?>> getMethodParams(Method method) {
        Map<String, Class<?>> map = new HashMap<>(0);
        for (Parameter parameter : method.getParameters()) {
            RequestParam param = parameter.getAnnotation(RequestParam.class);
            // TODO: 不使用注解匹配参数名字
            if (null == param) {
                throw new RuntimeException("必须有RequestParam指定的参数名");
            }
            map.put(param.value(), parameter.getType());
        }
        return map;
    }

    /**
     * 获取 HttpServletRequest 中的参数
     *
     * @param request HttpServletRequest
     * @return 参数别名对应的参数值
     */
    private Map<String, String> getRequestParams(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>(0);
        // GET、POST
        request.getParameterMap().forEach((paramName, paramsValues) -> {
            if (paramsValues != null) {
                paramMap.put(paramName, paramsValues[0]);
            }
        });
        // TODO: Body、Path、Header等方式的请求参数获取
        return paramMap;
    }

    /**
     * 实例化方法参数
     *
     * @param methodParams  方法的参数
     * @param requestParams request中的参数
     * @return 方法参数实例集合
     */
    private List<Object> instantiateMethodArgs(Map<String, Class<?>> methodParams, Map<String, String> requestParams) {
        return methodParams.keySet().stream().map(paramName -> {
            Class<?> type = methodParams.get(paramName);
            String requestValue = requestParams.get(paramName);
            Object value;
            if (null == requestValue) {
                value = primitiveNull(type);
            } else {
                value = convert(type, requestValue);
                // TODO: 实现非原生类的参数实例化
            }
            return value;
        }).collect(Collectors.toList());
    }

    /**
     * 返回基本数据类型的空值
     *
     * @param type 类
     * @return 对应的空值
     */
    private Object primitiveNull(Class<?> type) {
        if (type.equals(int.class) || type.equals(double.class) ||
            type.equals(short.class) || type.equals(long.class) ||
            type.equals(byte.class) || type.equals(float.class)) {
            return 0;
        }
        if (type.equals(boolean.class)) {
            return false;
        }
        return null;
    }


    /**
     * String类型转换成对应类型
     *
     * @param type  转换的类
     * @param value 值
     * @return 转换后的Object
     */
    private Object convert(Class<?> type, String value) {
        if (isPrimitive(type)) {
            if (value == null || EMPTY.equals(value)) {
                return primitiveNull(type);
            }
            if (type.equals(int.class) || type.equals(Integer.class)) {
                return Integer.parseInt(value);
            } else if (type.equals(String.class)) {
                return value;
            } else if (type.equals(Double.class) || type.equals(double.class)) {
                return Double.parseDouble(value);
            } else if (type.equals(Float.class) || type.equals(float.class)) {
                return Float.parseFloat(value);
            } else if (type.equals(Long.class) || type.equals(long.class)) {
                return Long.parseLong(value);
            } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
                return Boolean.parseBoolean(value);
            } else if (type.equals(Short.class) || type.equals(short.class)) {
                return Short.parseShort(value);
            } else if (type.equals(Byte.class) || type.equals(byte.class)) {
                return Byte.parseByte(value);
            }
            return value;
        } else {
            throw new RuntimeException("暂时不支持非原生");
        }
    }

    /**
     *
     * @param type
     * @return
     */
    private boolean isPrimitive(Class<?> type) {
        return type == boolean.class
            || type == Boolean.class
            || type == double.class
            || type == Double.class
            || type == float.class
            || type == Float.class
            || type == short.class
            || type == Short.class
            || type == int.class
            || type == Integer.class
            || type == long.class
            || type == Long.class
            || type == String.class
            || type == byte.class
            || type == Byte.class
            || type == char.class
            || type == Character.class;
    }
}
