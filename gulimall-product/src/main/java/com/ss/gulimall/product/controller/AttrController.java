package com.ss.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.ss.gulimall.product.vo.AttrRespVo;
import com.ss.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ss.gulimall.product.entity.AttrEntity;
import com.ss.gulimall.product.service.AttrService;
import com.ss.common.utils.PageUtils;
import com.ss.common.utils.R;

/**
 * 商品属性
 *
 * @author fangsheng
 * @email 445317262@qq.com
 * @date 2021-11-05 15:33:49
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
  @Autowired private AttrService attrService;

  /**
   * 获取分类规格参数
   *
   * @param params
   * @param catelogId
   * @return
   */
  @RequestMapping("base/list/{catelogId}")
  public R baseAttrList(
      @RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId) {
    PageUtils page =  attrService.queryBaseAttrPage(params, catelogId);
    return R.ok().put("page", page);
  }

  /** 列表 */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = attrService.queryPage(params);

    return R.ok().put("page", page);
  }

  /** 信息 */
  @RequestMapping("/info/{attrId}")
  public R info(@PathVariable("attrId") Long attrId) {
   AttrRespVo respVo = attrService.getInfo(attrId);
    return R.ok().put("respVo", respVo);
  }

  /** 保存 */
  @RequestMapping("/save")
  public R save(@RequestBody AttrVo attr) {
    attrService.saveAttr(attr);

    return R.ok();
  }

  /** 修改 */
  @RequestMapping("/update")
  public R update(@RequestBody AttrEntity attr) {
    attrService.updateById(attr);

    return R.ok();
  }

  /** 删除 */
  @RequestMapping("/delete")
  public R delete(@RequestBody Long[] attrIds) {
    attrService.removeByIds(Arrays.asList(attrIds));

    return R.ok();
  }
}
