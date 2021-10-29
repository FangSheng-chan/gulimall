package com.ss.gulimall.member.feign;

import com.ss.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author fangsheng
 * @date 2021/10/18 9:06 下午
 */
@Component
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

  @RequestMapping("coupon/coupon/member/list")
  public R memberCoupons();

}
