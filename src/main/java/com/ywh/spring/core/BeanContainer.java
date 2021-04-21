package com.ywh.spring.core;

import com.ywh.spring.aop.annotation.Aspect;
import com.ywh.spring.core.annotation.Component;
import com.ywh.spring.core.annotation.Controller;
import com.ywh.spring.core.annotation.Repository;
import com.ywh.spring.core.annotation.Service;
import com.ywh.spring.util.CommonUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.ywh.spring.util.CommonUtil.EMPTY;

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
    private static final List<Class<? extends Annotation>> BEAN_ANNOTATIONS = Arrays.asList(
        Component.class,
        Controller.class,
        Service.class,
        Repository.class,
        Aspect.class
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
     * @return
     */
    public List<Class<? extends Annotation>> getBeanAnnotations() {
        return BEAN_ANNOTATIONS;
    }

    /**
     *
     */
    public void loadBeans() {
        loadBeans(EMPTY);
    }

    /**
     * 扫描加载所有 Bean
     *
     * @param basePackage 包名
     */
    public void loadBeans(String basePackage) {
        if (isLoadBean) {
            return;
        }

        // 扫描根包下的所有类，判断其是否添加了组件注解，是则添加到集合中。
        CommonUtil.getPackageClass(basePackage).stream()
            .filter(clz -> {
                for (Class<? extends Annotation> annotation : BEAN_ANNOTATIONS) {
                    if (clz.isAnnotationPresent(annotation)) {
                        return true;
                    }
                }
                return false;
            })
            .forEach(clz -> beanMap.put(clz, CommonUtil.newInstance(clz)));

        // 添加成功修改标记为 true。
        isLoadBean = true;
    }

    /**
     * 获取 Bean 实例
     *
     * @param clz Class类型
     * @return Bean实例
     */
    public <T> T getBean(Class<T> clz) {
        if (null == clz) {
            return null;
        }
        return (T) beanMap.get(clz);
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
     */
    public void addBean(Class<?> clz, Object bean) {
        beanMap.put(clz, bean);
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
    public <T> Set<Class<T>> getClassesBySuper(Class<T> interfaceClass) {
        return beanMap.keySet()
            .stream()
            .filter(interfaceClass::isAssignableFrom)
            .filter(clz -> !clz.equals(interfaceClass))
            .map(v -> (Class<T>) v)
            .collect(Collectors.toSet());
    }
}
