package com.ss.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.ss.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ss.gulimall.product.entity.AttrGroupEntity;
import com.ss.gulimall.product.service.AttrGroupService;
import com.ss.common.utils.PageUtils;
import com.ss.common.utils.R;

/**
 * 属性分组
 *
 * @author fangsheng
 * @email 445317262@qq.com
 * @date 2021-11-05 15:33:49
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
  @Autowired private AttrGroupService attrGroupService;

  @Autowired private CategoryService categoryService;

  /** 列表 */
  @RequestMapping("/list/{catelogId}")
  public R list(
      @RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId) {
    PageUtils page = attrGroupService.queryPage(params, catelogId);
    return R.ok().put("page", page);
  }

  /** 信息 */
  @RequestMapping("/info/{attrGroupId}")
  public R info(@PathVariable("attrGroupId") Long attrGroupId) {
    AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
    // 用当前当前分类id查询完整路径并写入 attrGroup
    attrGroup.setCatelogPath(categoryService.findCateLogPath(attrGroup.getCatelogId()));
    return R.ok().put("attrGroup", attrGroup);
  }

  /** 保存 */
  @RequestMapping("/save")
  public R save(@RequestBody AttrGroupEntity attrGroup) {
    attrGroupService.save(attrGroup);

    return R.ok();
  }

  /** 修改 */
  @RequestMapping("/update")
  public R update(@RequestBody AttrGroupEntity attrGroup) {
    attrGroupService.updateById(attrGroup);

    return R.ok();
  }

  /** 删除 */
  @RequestMapping("/delete")
  public R delete(@RequestBody Long[] attrGroupIds) {
    attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

    return R.ok();
  }
}
