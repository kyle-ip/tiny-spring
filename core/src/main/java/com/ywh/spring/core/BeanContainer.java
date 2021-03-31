package com.ywh.spring.core;

import com.ywh.spring.core.annotation.Component;
import com.ywh.spring.core.annotation.Controller;
import com.ywh.spring.core.annotation.Repository;
import com.ywh.spring.core.annotation.Service;
import com.ywh.spring.util.ClassUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Bean 容器
 *
 * @author ywh
 * @since 31/03/2021
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanContainer {

    /**
     * 容器单例
     */
    private enum ContainerHolder {
        /**
         * 单例对象
         */
        INSTANCE;

        /**
         *
         */
        private final BeanContainer beanContainer;

        /**
         *
         */
        ContainerHolder() {
            beanContainer = new BeanContainer();
        }
    }

    /**
     * 存放所有 Bean 的 Map
     */
    private final Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

    /**
     * 是否加载 Bean
     */
    private boolean isLoadBean = false;

    /**
     * 加载 Bean 的注解列表
     */
    private static final List<Class<? extends Annotation>> BEAN_ANNOTATION = Arrays.asList(
        Component.class,
        Controller.class,
        Service.class,
        Repository.class
//        , Aspect.class
    );

    /**
     * 获取 Bean 容器实例
     *
     * @return BeanContainer
     */
    public static BeanContainer getInstance() {
        return ContainerHolder.INSTANCE.beanContainer;
    }

    /**
     * 扫描加载所有 Bean
     *
     * @param basePackage 包名
     */
    public void loadBeans(String basePackage) {
        if (isLoadBean) {
            log.warn("bean已经加载");
            return;
        }

        // 扫描根包下的所有类，判断其是否添加了组件注解，是则添加到集合中。
        Set<Class<?>> classSet = ClassUtil.getPackageClass(basePackage);
        classSet.stream()
            .filter(clz -> {
                for (Class<? extends Annotation> annotation : BEAN_ANNOTATION) {
                    if (clz.isAnnotationPresent(annotation)) {
                        return true;
                    }
                }
                return false;
            })
            .forEach(clz -> beanMap.put(clz, ClassUtil.newInstance(clz)));

        // 添加成功修改标记为 true。
        isLoadBean = true;
    }

    /**
     * 是否加载 Bean
     *
     * @return 是否加载
     */
    public boolean isLoadBean() {
        return isLoadBean;
    }

    /**
     * 获取 Bean 实例
     *
     * @param clz Class类型
     * @return Bean实例
     */
    public Object getBean(Class<?> clz) {
        if (null == clz) {
            return null;
        }
        return beanMap.get(clz);
    }

    /**
     * 获取所有 Bean 集合
     *
     * @return Bean 集合
     */
    public Set<Object> getBeans() {
        return new HashSet<>(beanMap.values());
    }

    /**
     * 添加一个 Bean 实例
     *
     * @param clz  Class 类型
     * @param bean Bean 实例
     * @return 原有的 Bean 实例, 没有则返回 null
     */
    public Object addBean(Class<?> clz, Object bean) {
        return beanMap.put(clz, bean);
    }

    /**
     * 移除一个 Bean 实例
     *
     * @param clz Class 类型
     */
    public void removeBean(Class<?> clz) {
        beanMap.remove(clz);
    }

    /**
     * Bean 实例数量
     *
     * @return 数量
     */
    public int size() {
        return beanMap.size();
    }

    /**
     * 所有 Bean 的 Class 集合
     *
     * @return Class 集合
     */
    public Set<Class<?>> getClasses() {
        return beanMap.keySet();
    }

    /**
     * 通过注解获取 Bean 的 Class 集合
     *
     * @param annotation 注解
     * @return Class集合
     */
    public Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation) {
        return beanMap.keySet()
            .stream()
            .filter(clz -> clz.isAnnotationPresent(annotation))
            .collect(Collectors.toSet());
    }

    /**
     * 通过实现类或者父类获取 Bean 的 Class 集合
     *
     * @param interfaceClass 接口 Class 或者父类 Class
     * @return Class 集合
     */
    public Set<Class<?>> getClassesBySuper(Class<?> interfaceClass) {
        return beanMap.keySet()
            .stream()
            .filter(interfaceClass::isAssignableFrom)
            .filter(clz -> !clz.equals(interfaceClass))
            .collect(Collectors.toSet());
    }
}
