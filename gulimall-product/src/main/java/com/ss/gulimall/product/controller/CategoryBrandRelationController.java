package com.ss.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ss.gulimall.product.entity.CategoryBrandRelationEntity;
import com.ss.gulimall.product.service.CategoryBrandRelationService;
import com.ss.common.utils.PageUtils;
import com.ss.common.utils.R;

/**
 * 品牌分类关联
 *
 * @author fangsheng
 * @email 445317262@qq.com
 * @date 2021-11-05 15:33:49
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
  @Autowired private CategoryBrandRelationService categoryBrandRelationService;

  /** 获取当前品牌关联的所有分类列表 */
  @RequestMapping("catelog/list")
  public R catelogList(@RequestParam("brandId") Long brandId) {
    List<CategoryBrandRelationEntity> data =
        categoryBrandRelationService.list(
            new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));
    return R.ok().put("data", data);
  }



  /** 列表 */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = categoryBrandRelationService.queryPage(params);

    return R.ok().put("page", page);
  }

  /** 信息 */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Long id) {
    CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

    return R.ok().put("categoryBrandRelation", categoryBrandRelation);
  }

  /** 保存 */
  @RequestMapping("/save")
  public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
    categoryBrandRelationService.saveDetail(categoryBrandRelation);

    return R.ok();
  }

  /** 修改 */
  @RequestMapping("/update")
  public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
    categoryBrandRelationService.updateById(categoryBrandRelation);

    return R.ok();
  }

  /** 删除 */
  @RequestMapping("/delete")
  public R delete(@RequestBody Long[] ids) {
    categoryBrandRelationService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }
}
