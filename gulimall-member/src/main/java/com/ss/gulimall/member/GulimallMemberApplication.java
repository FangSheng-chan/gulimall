package com.ss.gulimall.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author fangsheng
 * @date 2021/10/17 1:58 下午
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.ss.gulimall.member.feign")
@MapperScan("com.ss.gulimall.member.dao")
public class GulimallMemberApplication {

  public static void main(String[] args) {
    try {
      SpringApplication.run(GulimallMemberApplication.class, args);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }
}
