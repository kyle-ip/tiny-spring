package com.ywh.spring.ioc;

import com.ywh.spring.core.BeanContainer;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * IOC 启动类
 *
 * @author ywh
 * @since 31/03/2021
 */
public class IOC {

    /**
     * Bean容器
     */
    private final BeanContainer beanContainer;

    /**
     *
     */
    public IOC() {
        beanContainer = BeanContainer.getInstance();
    }

    /**
     *
     * @param basePackage
     */
    public void doIOC(String basePackage) {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans(basePackage);
        doIOC();
    }

    /**
     * 执行 IOC
     */
    public void doIOC() {
        for (Class<?> clazz : beanContainer.getClasses()) {
            // 从 Bean 容器中获取所有的类及其对应的 Bean。
            final Object bean = beanContainer.getBean(clazz);
            Field[] fields = clazz.getDeclaredFields();

            // 取出 Bean 所有的域。
            for (Field field : fields) {

                // 如果该 Bean 中的域被添加上 Autowired 注解，则获取其类型。
                if (field.isAnnotationPresent(Autowired.class)) {
                    final Class<?> fieldClass = field.getType();

                    // 根据域的类型，获取其对象值，设置到 Bean 的域中。
                    Object fieldValue = getClassInstance(fieldClass);
                    if (null != fieldValue) {
                        field.setAccessible(true);
                        try {
                            field.set(bean, fieldValue);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        throw new RuntimeException("无法注入对应的类，目标类型:" + fieldClass.getName());
                    }
                }
            }
        }
    }

    /**
     * 根据 Class 获取其实例或者实现类
     *
     * @param clazz Class
     * @return 实例或者实现类
     */
    private <T> T getClassInstance(final Class<T> clazz) {
        return Optional
            // 从 Bean 容器中获取 Bean 对象实例。
            .ofNullable(beanContainer.getBean(clazz))
            // 获取失败，可能为接口，因此获取接口的实现类（取第一个）。
            .orElseGet(() -> {
                Class<?> implementClass = beanContainer.getClassesBySuper(clazz)
                    .stream()
                    .findFirst()
                    .orElse(null);
                if (null != implementClass) {
                    return (T) beanContainer.getBean(implementClass);
                }
                return null;
            });
    }
}
