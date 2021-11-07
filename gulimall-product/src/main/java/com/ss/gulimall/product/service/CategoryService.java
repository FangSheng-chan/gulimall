package com.ss.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ss.common.utils.PageUtils;
import com.ss.gulimall.product.entity.CategoryEntity;

import java.util.Map;

/**
 * 商品三级分类
 *
 * @author fangsheng
 * @email 445317262@qq.com
 * @date 2021-11-05 15:33:49
 */
public interface CategoryService extends IService<CategoryEntity> {

  PageUtils queryPage(Map<String, Object> params);

  /**
   * 找到catelogId的完整路径 【父/子/孙】
   *
   * @param catelogId
   * @return
   */
  Long[] findCateLogPath(Long catelogId);

    void updateCascade(CategoryEntity category);
}
