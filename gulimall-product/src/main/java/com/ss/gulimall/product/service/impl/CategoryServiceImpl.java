package com.ss.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ss.gulimall.product.service.CategoryBrandRelationService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ss.common.utils.PageUtils;
import com.ss.common.utils.Query;

import com.ss.gulimall.product.dao.CategoryDao;
import com.ss.gulimall.product.entity.CategoryEntity;
import com.ss.gulimall.product.service.CategoryService;
import com.ss.gulimall.product.vo.Catelog2Vo;

import org.springframework.transaction.annotation.Transactional;

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity>
    implements CategoryService {

  @Autowired CategoryBrandRelationService categoryBrandRelationService;

  @Autowired StringRedisTemplate redisTemplate;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<CategoryEntity> page =
        this.page(new Query<CategoryEntity>().getPage(params), new QueryWrapper<CategoryEntity>());

    return new PageUtils(page);
  }

  @Override
  public Long[] findCateLogPath(Long catelogId) {
    List<Long> paths = new ArrayList<>();
    paths = findParentPath(catelogId, paths);
    Collections.reverse(paths);
    return paths.toArray(new Long[paths.size()]);
  }

  /**
   * 级联更新所有关联的数据
   *
   * @param category
   */
  @Transactional(rollbackFor = Throwable.class)
  @Override
  public void updateCascade(CategoryEntity category) {
    this.updateById(category);
    categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
  }

  @Override
  public List<CategoryEntity> getLevel1() {
    List<CategoryEntity> categoryEntities =
        baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
    return categoryEntities;
  }

  /**
   * 缓存中间件 redis
   *
   * <p>缓存存的都是 JSON
   *
   * @return
   */
  @Override
  public Map<String, List<Catelog2Vo>> getCatelogJson() {
    return getDataFromDB();
  }

  /**
   * 递归收集所有父节点
   *
   * @param catelogId
   * @param paths
   * @return
   */
  private List<Long> findParentPath(Long catelogId, List<Long> paths) {
    paths.add(catelogId);
    /**
     * 执行SQL SELECT
     * cat_id,parent_cid,icon,name,show_status,product_unit,sort,product_count,cat_level FROM
     * pms_category WHERE cat_id=?
     */
    CategoryEntity byId = this.getById(catelogId);
    if (byId.getParentCid() != 0) {
      findParentPath(byId.getParentCid(), paths);
    }
    return paths;
  }

  /**
   * 分布式程锁Redis
   *
   * @return
   */
  public Map<String, List<Catelog2Vo>> getCatelogJsonFromDbWithRedisLock() {
    // 占分布式锁,同时设置锁过期时间,必须和加锁同步原子操作
    String uuid = UUID.randomUUID().toString();
    Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", "111", 300, TimeUnit.SECONDS);
    if (lock) {
      // 加锁成功,执行业务
      Map<String, List<Catelog2Vo>> dataFromDB;
      try {
        dataFromDB = getDataFromDB();
      } finally {
        String script =
            "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1])else return 0 end";
        // 删除锁，Lua脚本
        Long lock1 =
            redisTemplate.execute(
                new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock", uuid));
      }
      return dataFromDB;
    } else {
      // 加锁失败,休眠2秒,重试
      try {
        Thread.sleep(2000);
      } catch (Exception e) {
        e.printStackTrace();
      }
      // 自旋
      return getCatelogJsonFromDbWithRedisLock();
    }
  }

  private Map<String, List<Catelog2Vo>> getDataFromDB() {
    /** 1、空结果缓存，解决缓存穿透 2、设置过期时间（加随机值），解决缓存穿透 3、加锁，解决缓存击穿 */
    String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
    if (!StringUtils.isEmpty(catalogJSON)) {
      System.out.println("缓存命中");
      Map<String, List<Catelog2Vo>> result =
          JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {});
      return result;
    }
    System.out.println("查询了数据库");
    /** 优化:将数据库中的多次查询变为一次,存至缓存selectList,需要的数据从list取出,避免频繁的数据库交互 */
    List<CategoryEntity> selectList = baseMapper.selectList(null);
    // 1.查出所有1级分类
    List<CategoryEntity> level1 = getParent_cid(selectList, 0L);
    // 2.封装数据
    Map<String, List<Catelog2Vo>> parent_cid =
        level1.stream()
            .collect(
                Collectors.toMap(
                    k -> k.getCatId().toString(),
                    v -> {
                      // 1.查出1级分类中所有2级分类
                      List<CategoryEntity> categoryEntities =
                          getParent_cid(selectList, v.getCatId());
                      // 2.封装上面的结果
                      List<Catelog2Vo> catelog2Vos = null;
                      if (categoryEntities != null) {
                        catelog2Vos =
                            categoryEntities.stream()
                                .map(
                                    l2 -> {
                                      Catelog2Vo catelog2Vo =
                                          new Catelog2Vo(
                                              v.getCatId().toString(),
                                              null,
                                              l2.getCatId().toString(),
                                              l2.getName());
                                      // 查询当前2级分类的3级分类
                                      List<CategoryEntity> level3 =
                                          getParent_cid(selectList, l2.getCatId());
                                      if (level3 != null) {
                                        List<Catelog2Vo.Catelog3Vo> collect =
                                            level3.stream()
                                                .map(
                                                    l3 -> {
                                                      // 封装指定格式
                                                      Catelog2Vo.Catelog3Vo catelog3Vo =
                                                          new Catelog2Vo.Catelog3Vo(
                                                              l2.getCatId().toString(),
                                                              l3.getCatId().toString(),
                                                              l3.getName());
                                                      return catelog3Vo;
                                                    })
                                                .collect(Collectors.toList());
                                        catelog2Vo.setCatalog3List(collect);
                                      }
                                      return catelog2Vo;
                                    })
                                .collect(Collectors.toList());
                      }
                      return catelog2Vos;
                    }));
    String s = JSON.toJSONString(parent_cid);
    redisTemplate.opsForValue().set("catalogJSON", s, 1, TimeUnit.DAYS);
    return parent_cid;
  }

  private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList, Long parent_cid) {
    List<CategoryEntity> collect =
        selectList.stream()
            .filter(item -> Objects.equals(item.getParentCid(), parent_cid))
            .collect(Collectors.toList());
    return collect;
  }
}
