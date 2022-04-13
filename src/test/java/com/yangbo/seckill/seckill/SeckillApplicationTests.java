package com.yangbo.seckill.seckill;

import com.yangbo.seckill.seckill.controller.AOPHelloController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class SeckillApplicationTests {

    @Autowired
    AOPHelloController aopHelloController;

    @Test
    void contextLoads() {
        aopHelloController.getHello();
    }
    
    @Test
    void testPost(){
        List<? extends CharSequence> list = new ArrayList<String>();
        List<? super String> list1 = new ArrayList<CharSequence>();
    }

}
