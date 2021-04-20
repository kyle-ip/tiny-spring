package com.ywh.spring;

import com.ywh.spring.aop.ProxyAdvisor;
import lombok.Getter;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 通知链
 *
 * @author ywh
 * @since 4/20/2021
 */
public class AdviceChain {


    /**
     * 目标类
     */
    @Getter
    private final Class<?> targetClass;
    /**
     * 目标实例
     */
    @Getter
    private final Object target;
    /**
     * 目标方法
     */
    @Getter
    private final Method method;
    /**
     * 目标方法参数
     */
    @Getter
    private final Object[] args;
    /**
     * 代理方法
     */
    private final MethodProxy methodProxy;
    /**
     * 代理通知列
     */
    private List<ProxyAdvisor> proxyList;
    /**
     * 代理通知列index
     */
    private int adviceIndex = 0;

    public AdviceChain(Class<?> targetClass, Object target, Method method, Object[] args, MethodProxy methodProxy, List<ProxyAdvisor> proxyList) {
        this.targetClass = targetClass;
        this.target = target;
        this.method = method;
        this.args = args;
        this.methodProxy = methodProxy;
        this.proxyList = proxyList;
    }

    /**
     * 递归执行 执行代理通知列
     *
     * @return 目标方法的结果
     * @throws Throwable Throwable
     */
    public Object doAdviceChain() throws Throwable {
        while (adviceIndex < proxyList.size() && !proxyList.get(adviceIndex).getPointcut().matches(method)) {
            // 如果当前方法不匹配切点，则略过该代理通知类。
            adviceIndex++;
        }
        return adviceIndex < proxyList.size()? proxyList.get(adviceIndex++).doProxy(this): methodProxy.invokeSuper(target, args);
    }
}
