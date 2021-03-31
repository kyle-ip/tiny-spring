package com.ywh.spring.core;

import com.ywh.spring.core.annotation.Service;

/**
 * @author ywh
 * @since 31/03/2021
 */
@Service
public class TestServiceImpl implements TestService {

    /**
     *
     * @return
     */
    @Override
    public String say() {
        return "Hello, World!";
    }
}
