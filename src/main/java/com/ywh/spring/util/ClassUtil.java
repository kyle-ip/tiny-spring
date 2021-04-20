package com.ywh.spring.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;

/**
 * 类操作工具类
 *
 * @author ywh
 * @since 31/03/2021
 */
@Slf4j
public class ClassUtil {


    /**
     * file 形式 url 协议
     */
    public static final String FILE_PROTOCOL = "file";

    /**
     * jar 形式 url 协议
     */
    public static final String JAR_PROTOCOL = "jar";

    /**
     * 获取 ClassLoader
     *
     * @return 当前 ClassLoader
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获取 Class
     *
     * @param className class 全名
     * @return Class
     */
    public static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("load class error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 实例化 class
     *
     * @param clz Class
     * @param <T>   class的类型
     * @return 类的实例化
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clz) {
        try {
            return (T) clz.getDeclaredConstructor().newInstance();
            // deprecated in Java 9: return (T) clz.newInstance();
        } catch (Exception e) {
            log.error("newInstance error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置类的属性值
     *
     * @param field      属性
     * @param target     类实例
     * @param value      值
     * @param accessible 是否允许设置私有属性
     */
    public static void setField(Field field, Object target, Object value, boolean accessible) {
        field.setAccessible(accessible);
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            log.error("setField error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取包下的类集合
     *
     * @param basePackage 包的路径
     * @return 类集合
     */
    public static Set<Class<?>> getPackageClass(String basePackage) {
        URL url = getClassLoader().getResource(basePackage.replace(".", "/"));
        if (null == url) {
            throw new RuntimeException("无法获取项目路径文件");
        }

        try {
            if (url.getProtocol().equalsIgnoreCase(FILE_PROTOCOL)) {
                File file = new File(url.getFile());
                Path basePath = file.toPath();
                return Files.walk(basePath)
                    .filter(path -> path.toFile().getName().endsWith(".class"))
                    .map(path -> getClassByPath(path, basePath, basePackage))
                    .collect(Collectors.toSet());
            } else if (url.getProtocol().equalsIgnoreCase(JAR_PROTOCOL)) {
                // 若在 jar 包中，则解析 jar 包中的 entry。
                return ((JarURLConnection) url.openConnection()).getJarFile()
                    .stream()
                    .filter(jarEntry -> jarEntry.getName().endsWith(".class"))
                    .map(ClassUtil::getClassByJar)
                    .collect(Collectors.toSet());
            }
            return Collections.emptySet();
        } catch (IOException e) {
            log.error("load package error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 从 Path 获取 Class
     *
     * @param classPath   类的路径
     * @param basePath    包目录的路径
     * @param basePackage 包名
     * @return 类
     */
    private static Class<?> getClassByPath(Path classPath, Path basePath, String basePackage) {
        String packageName = classPath.toString().replace(basePath.toString(), "");
        String className = (basePackage + packageName)
            .replace("/", ".")
            .replace("\\", ".")
            .replace(".class", "");
        // 如果 class 在根目录要去除最前面的。
        className = className.charAt(0) == '.' ? className.substring(1) : className;
        return loadClass(className);
    }

    /**
     * 从 jar 包获取 Class
     *
     * @param jarEntry jar 文件
     * @return 类
     */
    private static Class<?> getClassByJar(JarEntry jarEntry) {
        String jarEntryName = jarEntry.getName();
        // 获取类名
        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
        return loadClass(className);
    }
}
