package com.ywh.spring.mvc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请求相关信息
 *
 * @author ywh
 * @since 4/20/2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PathInfo {

    /**
     * 请求方法
     */
    private String httpMethod;

    /**
     * 请求路径
     */
    private String httpPath;
}
