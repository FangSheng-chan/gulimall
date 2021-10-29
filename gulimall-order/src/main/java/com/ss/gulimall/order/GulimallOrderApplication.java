package com.ss.gulimall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fangsheng
 * @date 2021/10/17 4:25 下午
 */
@SpringBootApplication
@MapperScan("com.ss.gulimall.order.dao")
public class GulimallOrderApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(GulimallOrderApplication.class, args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
