package com.ss.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ss.gulimall.product.dao.BrandDao;
import com.ss.gulimall.product.dao.CategoryDao;
import com.ss.gulimall.product.entity.BrandEntity;
import com.ss.gulimall.product.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ss.common.utils.PageUtils;
import com.ss.common.utils.Query;

import com.ss.gulimall.product.dao.CategoryBrandRelationDao;
import com.ss.gulimall.product.entity.CategoryBrandRelationEntity;
import com.ss.gulimall.product.service.CategoryBrandRelationService;

@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl
    extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity>
    implements CategoryBrandRelationService {

  @Autowired private CategoryDao categoryDao;

  @Autowired private BrandDao brandDao;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<CategoryBrandRelationEntity> page =
        this.page(
            new Query<CategoryBrandRelationEntity>().getPage(params),
            new QueryWrapper<CategoryBrandRelationEntity>());

    return new PageUtils(page);
  }

  @Override
  public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
    Long brandId = categoryBrandRelation.getBrandId();
    Long catelogId = categoryBrandRelation.getCatelogId();
    // 1、查询详细名字
    BrandEntity brandEntity = brandDao.selectById(catelogId);
    CategoryEntity categoryEntity = categoryDao.selectById(brandId);
    categoryBrandRelation.setBrandName(brandEntity.getName());
    categoryBrandRelation.setCatelogName(categoryEntity.getName());
    this.save(categoryBrandRelation);
  }

  @Override
  public void updataBrand(Long brandId, String name) {
    CategoryBrandRelationEntity re = new CategoryBrandRelationEntity();
    re.setBrandId(brandId);
    re.setBrandName(name);
    this.update(re, new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));
  }

  @Override
  public void updateCategory(Long catId, String name) {
    this.baseMapper.updateCategory(catId, name);
  }
}
