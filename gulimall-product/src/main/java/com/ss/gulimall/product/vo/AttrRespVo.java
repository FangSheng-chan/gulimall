package com.ss.gulimall.product.vo;

import lombok.Data;

/**
 * @author fangsheng
 * @date 2021/11/7 11:07 PM
 */
@Data
public class AttrRespVo extends AttrVo {
  /** "catelogName": "手机/数码/手机", //所属分类名字 */
  private String catelogName;
  /** "groupName": "主体", //所属分组名字 */
  private String groupName;

  private Long[] catelogPath;
}
