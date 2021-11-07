package com.ss.gulimall.product.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ss.common.utils.PageUtils;
import com.ss.common.utils.Query;

import com.ss.gulimall.product.dao.AttrGroupDao;
import com.ss.gulimall.product.entity.AttrGroupEntity;
import com.ss.gulimall.product.service.AttrGroupService;

@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity>
    implements AttrGroupService {

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<AttrGroupEntity> page =
        this.page(
            new Query<AttrGroupEntity>().getPage(params), new QueryWrapper<AttrGroupEntity>());

    return new PageUtils(page);
  }

  @Override
  public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
    // key为搜索栏的 检索的关键词
    String key = (String) params.get("key");
    QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
    // key不为空
    if (!StringUtils.isEmpty(key)) {
      /**
       * sql语句
       *
       * <p>select * from pms_attr_group where catelog_id = ? and ( attr_group_id = key or
       * attr_group_name like %key% )
       */
      wrapper.and((obj) -> obj.eq("attr_group_id", key).or().like("attr_group_name", key));
    }
    if (catelogId == 0) {
      // Query可以把map封装为IPage
      /** 查哪张表就用哪个实体类 */
      IPage<AttrGroupEntity> page =
          this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
      return new PageUtils(page);
    } else {
      // 增加id信息
      wrapper.eq("catelog_id", catelogId);
      IPage<AttrGroupEntity> page =
          this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
      return new PageUtils(page);
    }
  }
}
