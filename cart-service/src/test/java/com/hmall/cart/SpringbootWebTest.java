package com.hmall.cart;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;

@SpringBootTest
public class SpringbootWebTest {

    @Resource
    private ApplicationContext applicationContext;
    @Test
    public void testScope() {
        for (int i = 0; i < 50; i++) {
            Object cartController = applicationContext.getBean("cartController");
            System.out.println(cartController);
        }
    }
}
