package com.ss.gulimall.product.service.impl;

import com.ss.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ss.common.utils.PageUtils;
import com.ss.common.utils.Query;

import com.ss.gulimall.product.dao.CategoryDao;
import com.ss.gulimall.product.entity.CategoryEntity;
import com.ss.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity>
    implements CategoryService {

  @Autowired CategoryBrandRelationService categoryBrandRelationService;

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
}
