package com.ss.gulimall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fangsheng
 * @date 2021/10/17 1:58 下午
 */
@SpringBootApplication
@MapperScan("com.ss.gulimall.ware.dao")
public class GulimallWareApplication {

  public static void main(String[] args) {
    SpringApplication.run(GulimallWareApplication.class, args);
  }
}
