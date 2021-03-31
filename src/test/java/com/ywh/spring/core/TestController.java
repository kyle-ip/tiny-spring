package com.ywh.spring.core;

import com.ywh.spring.core.annotation.Controller;
import com.ywh.spring.ioc.Autowired;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ywh
 * @since 31/03/2021
 */

@Slf4j
@Controller
public class TestController {

    /**
     *
     */
    @Autowired
    private TestService testService;

    /**
     *
     * @param word
     * @return
     */
    public String say(String word) {
        System.out.println(word);
        return testService.say(word);
    }
}
