package com.ywh.spring.ioc;

import com.ywh.spring.core.BeanContainer;
import com.ywh.spring.core.TestController;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
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
    public void doIOCTest() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans();
        new IOC().doIOC();
        TestController controller = BeanContainer.getInstance().getBean(TestController.class);
        Assert.assertEquals("ywh", controller.say("ywh"));
    }
}
