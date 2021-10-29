package com.ss.gulimall.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author fangsheng
 * @date 2021/10/17 1:58 下午
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.ss.gulimall.coupon.dao")
public class GulimallCouponApplication {

  public static void main(String[] args) {
    SpringApplication.run(GulimallCouponApplication.class, args);
  }
}
