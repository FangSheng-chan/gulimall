package com.ss.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fangsheng
 * @date 2021/10/26 4:11 下午
 */
@SpringBootApplication
@MapperScan("com.ss.gulimall.product.dao")
public class GulimallProductApplication {
  public static void main(String[] args) {
    try {
      SpringApplication.run(GulimallProductApplication.class, args);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }
}
