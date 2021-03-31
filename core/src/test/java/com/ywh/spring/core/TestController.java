package com.ywh.spring.core;

import com.ywh.spring.core.annotation.Controller;
import com.ywh.spring.ioc.Autowired;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ywh
 * @since 31/03/2021
 */
@Controller
@Slf4j
public class TestController {

    /**
     *
     */
    @Autowired
    private TestService testService;

    /**
     *
     */
    public void hello() {
        log.info(testService.say());
    }
}
