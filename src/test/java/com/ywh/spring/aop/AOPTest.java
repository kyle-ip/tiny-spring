package com.ywh.spring.aop;

import com.ywh.spring.core.BeanContainer;
import com.ywh.spring.core.TestController;
import com.ywh.spring.ioc.IOC;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author ywh
 * @since 31/03/2021
 */
@Slf4j
public class AOPTest {

    @Test
    public void doAop() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans();
        new AOP().doAOP();
        new IOC().doIOC();
        TestController controller = BeanContainer.getInstance().getBean(TestController.class);
        controller.say("Hello, World!");
    }
}
