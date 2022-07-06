package com.ss.gulimall.product;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class GulimallProductApplicationTests {

  @Autowired StringRedisTemplate redisTemplate;

  @Autowired RedissonClient redissonClient;

  @Test
  void contextLoads() {
    redisTemplate.opsForValue().set("ss", "dd");
    String value = redisTemplate.opsForValue().get("ss");
    System.out.println(value);
  }

  @Test
  void t1() {
    System.out.println(redissonClient);
  }
}
