package com.ss.gulimall.product.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fangsheng
 * @date 2022/6/27 11:30 AM
 */
@AllArgsConstructor
@Data
public class Catelog2Vo {
  /**
   *  1级父分类
   */
  private String catalog1Id;
  /**
   *  3级子分类
   */
  private List<Catelog3Vo> catalog3List;
  private String id;
  private String name;

  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  public static class Catelog3Vo {
    private String catalog2Id;
    private String id;
    private String name;
  }
}
