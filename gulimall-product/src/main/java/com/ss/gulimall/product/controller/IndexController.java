package com.ss.gulimall.product.controller;

import com.ss.gulimall.product.service.CategoryService;
import com.ss.gulimall.product.vo.Catelog2Vo;

import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author fangsheng
 * @date 2022/6/27 11:26 AM
 */
@RestController
public class IndexController {

  @Autowired CategoryService categoryService;

  @Autowired RedissonClient redissonClient;

  @Autowired StringRedisTemplate redisTemplate;

  @GetMapping("/")
  public String index() {
    // 获得锁
    RLock lock = redissonClient.getLock("r-lock");
    // 加锁 阻塞等待
    //    lock.lock();
    lock.lock(30, TimeUnit.SECONDS);
    try {
      System.out.println("加锁成功");
      Thread.sleep(10000);
    } catch (Exception e) {
    } finally {
      System.out.println("释放锁");
      lock.unlock();
    }
    return "/";
  }

  @GetMapping("/write")
  public String writeValue() {
    String uuid = UUID.randomUUID().toString();
    RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
    RLock rLock = lock.writeLock();
    try {
      rLock.lock();
      Thread.sleep(30000);
      redisTemplate.opsForValue().set("writeValue", uuid);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      rLock.unlock();
    }
    return uuid;
  }

  @GetMapping("/read")
  public String readValue() {
    String s = "";
    RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
    RLock rLock = lock.readLock();
    try {
      rLock.lock();
      Thread.sleep(30000);
      s = redisTemplate.opsForValue().get("writeValue");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      rLock.unlock();
    }
    return s;
  }

  /**
   * countDownLatch
   *
   * @return
   */
  @GetMapping("/lockDoor")
  public String lockDoor() {
    RCountDownLatch door = redissonClient.getCountDownLatch("door");
    door.trySetCount(5);
    try {
      door.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
    }
    return "放假了.....";
  }

  @GetMapping("/gogogo/{id}")
  public String gogogo(@PathVariable("id") Long id) {
    RCountDownLatch door = redissonClient.getCountDownLatch("door");
    door.countDown();
    return id + "班的人都走了";
  }

  /**
   * Semaphore 限流的作用
   *
   * @return
   */
  @GetMapping("/park")
  public String park() {
    RSemaphore park = redissonClient.getSemaphore("park");
    Semaphore semaphore = new Semaphore(10);
    try {
      park.acquire();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return "停车成功";
  }

  @GetMapping("/go")
  public String go() {
    RSemaphore park = redissonClient.getSemaphore("park");
    park.release();
    return "走了走了";
  }

  /**
   * 缓存要加过期时间
   *
   * @return
   */
  @GetMapping("/index/catalog.json")
  public Map<String, List<Catelog2Vo>> getCatelogJson() {
    Map<String, List<Catelog2Vo>> catelogJson = categoryService.getCatelogJson();
    return catelogJson;
  }
}
