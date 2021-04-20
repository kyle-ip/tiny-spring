package com.ywh.spring.example.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ywh
 * @since 4/20/2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    T data;

    int code;

    String msg;
}
