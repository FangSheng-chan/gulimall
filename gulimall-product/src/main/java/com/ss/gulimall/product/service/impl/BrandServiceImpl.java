package com.ss.gulimall.product.service.impl;

import com.ss.gulimall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ss.common.utils.PageUtils;
import com.ss.common.utils.Query;

import com.ss.gulimall.product.dao.BrandDao;
import com.ss.gulimall.product.entity.BrandEntity;
import com.ss.gulimall.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;

@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

  @Autowired CategoryBrandRelationService categoryBrandRelationService;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {

    String key = (String) params.get("key");
    QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
    if (StringUtils.isNotBlank(key)) {
      wrapper.eq("brand_id", key).or().like("name", key);
    }
    IPage<BrandEntity> page = this.page(new Query<BrandEntity>().getPage(params), wrapper);
    return new PageUtils(page);
  }

  @Transactional(rollbackFor = Throwable.class)
  @Override
  public void updateDetail(BrandEntity brand) {
    // 考虑冗余字段的数据一致
    this.updateById(brand);
    if (StringUtils.isNotEmpty(brand.getName())) {
      // 同步更新其他关联表中的数据
      categoryBrandRelationService.updataBrand(brand.getBrandId(), brand.getName());
      // TODO 更新其他关联
    }
  }
}
