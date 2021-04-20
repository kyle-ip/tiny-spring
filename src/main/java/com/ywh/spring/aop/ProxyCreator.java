package com.ywh.spring.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理类创建器
 *
 * @author ywh
 * @since 31/03/2021
 */
public class ProxyCreator {


    /**
     * 创建代理类
     *
     * @param targetClass 目标类
     * @param proxyList   代理通知列表
     * @return 代理类
     */
    public static Object createProxy(Class<?> targetClass, List<ProxyAdvisor> proxyList) {
        return Enhancer.create(targetClass, new AdviceMethodInterceptor(targetClass, proxyList));
    }

    /**
     * cglib MethodInterceptor实现类
     */
    private static class AdviceMethodInterceptor implements MethodInterceptor {

        /**
         * 目标类
         */
        private final Class<?> targetClass;

        /**
         * 代理通知列表
         */
        private List<ProxyAdvisor> proxyList;

        /**
         * @param targetClass
         * @param proxyList
         */
        public AdviceMethodInterceptor(Class<?> targetClass, List<ProxyAdvisor> proxyList) {
            this.targetClass = targetClass;
            this.proxyList = proxyList;
        }

        /**
         * @param target
         * @param method
         * @param args
         * @param proxy
         * @return
         * @throws Throwable
         */
        @Override
        public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            return new AdviceChain(targetClass, target, method, args, proxy, proxyList).doAdviceChain();
        }
    }
}
