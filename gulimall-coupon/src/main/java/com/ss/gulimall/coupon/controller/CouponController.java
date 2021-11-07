package com.ss.gulimall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ss.gulimall.coupon.entity.CouponEntity;
import com.ss.gulimall.coupon.service.CouponService;
import com.ss.common.utils.PageUtils;
import com.ss.common.utils.R;

/**
 * 优惠券信息
 *
 * @author fangsheng
 * @email 445317262@qq.com
 * @date 2021-10-17 15:23:09
 */
@RestController
@RequestMapping("coupon/coupon")
public class CouponController {
  @Autowired private CouponService couponService;

  @Value("${user.name}")
  private String name;

  @Value("${user.age}")
  private Integer age;

  @RequestMapping("/test")
  public R getConfig() {
    return R.ok().put("name", name).put("age", age);
  }

  @RequestMapping("/member/list")
  public R memberCoupons() {
    CouponEntity couponEntity = new CouponEntity();
    couponEntity.setCouponName("满100减10");
    return R.ok().put("coupons", Arrays.asList(couponEntity));
  }

  /** 列表 */
  @RequestMapping("/list")
  public R list(@RequestParam Map<String, Object> params) {
    PageUtils page = couponService.queryPage(params);

    return R.ok().put("page", page);
  }

  /** 信息 */
  @RequestMapping("/info/{id}")
  public R info(@PathVariable("id") Long id) {
    CouponEntity coupon = couponService.getById(id);

    return R.ok().put("coupon", coupon);
  }

  /** 保存 */
  @RequestMapping("/save")
  public R save(@RequestBody CouponEntity coupon) {
    couponService.save(coupon);

    return R.ok();
  }

  /** 修改 */
  @RequestMapping("/update")
  public R update(@RequestBody CouponEntity coupon) {
    couponService.updateById(coupon);

    return R.ok();
  }

  /** 删除 */
  @RequestMapping("/delete")
  public R delete(@RequestBody Long[] ids) {
    couponService.removeByIds(Arrays.asList(ids));

    return R.ok();
  }
}
