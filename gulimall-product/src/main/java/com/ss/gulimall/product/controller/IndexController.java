package com.ss.gulimall.product.controller;

import com.ss.gulimall.product.config.RedissonConfig;
import com.ss.gulimall.product.service.CategoryService;
import com.ss.gulimall.product.vo.Catelog2Vo;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author fangsheng
 * @date 2022/6/27 11:26 AM
 */
@RestController
public class IndexController {

  @Autowired CategoryService categoryService;

  @Autowired RedissonClient redissonClient;

  @GetMapping("/")
  public String index() {
    // 获得锁
    RLock lock = redissonClient.getLock("r-lock");
    // 加锁 阻塞等待
    lock.lock();
    try {
      System.out.println("加锁成功");
      Thread.sleep(30000);
    } catch (Exception e) {
    } finally {
      System.out.println("释放锁");
      lock.unlock();
    }
    return "hello";
  }

  @ResponseBody
  @GetMapping("/index/catalog.json")
  public Map<String, List<Catelog2Vo>> getCatelogJson() {
    Map<String, List<Catelog2Vo>> catelogJson = categoryService.getCatelogJson();
    return catelogJson;
  }
}
