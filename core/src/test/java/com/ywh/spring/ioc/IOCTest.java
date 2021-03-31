package com.ywh.spring.ioc;

import com.ywh.spring.core.BeanContainer;
import com.ywh.spring.core.TestController;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author ywh
 * @since 31/03/2021
 */
@Slf4j
public class IOCTest {

    /**
     *
     */
    @Test
    public void doIocTest() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("com.ywh");
        new IOC().doIoc();
        TestController controller = (TestController) beanContainer.getBean(TestController.class);
        controller.hello();
    }
}
