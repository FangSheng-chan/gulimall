package com.ss.gulimall.product.service.impl;

import com.ss.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.ss.gulimall.product.dao.AttrGroupDao;
import com.ss.gulimall.product.dao.CategoryDao;
import com.ss.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.ss.gulimall.product.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ss.common.utils.PageUtils;
import com.ss.common.utils.Query;

import com.ss.gulimall.product.dao.AttrDao;
import com.ss.gulimall.product.entity.AttrEntity;
import com.ss.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;

@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

  @Autowired AttrAttrgroupRelationDao attrAttrgroupRelationDao;

  @Autowired AttrGroupDao attrGroupDao;

  @Autowired CategoryDao categoryDao;

  @Override
  public PageUtils queryPage(Map<String, Object> params) {
    IPage<AttrEntity> page =
        this.page(new Query<AttrEntity>().getPage(params), new QueryWrapper<AttrEntity>());

    return new PageUtils(page);
  }

  @Transactional(rollbackFor = Throwable.class)
  @Override
  public void saveAttr(AttrVo attr) {
    AttrEntity attrEntity = new AttrEntity();
    BeanUtils.copyProperties(attr, attrEntity);
    this.save(attrEntity);
    AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
    relationEntity.setAttrGroupId(attr.getAttrGroupId());
    relationEntity.setAttrId(attr.getAttrId());
    attrAttrgroupRelationDao.insert(relationEntity);
  }

  @Override
  public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId) {
    QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
    if (catelogId != 0) {
      queryWrapper.eq("catelog_id", catelogId);
    }
    String key = (String) params.get("key");
    if (StringUtils.isNotEmpty(key)) {
      queryWrapper.and(
          (wrapper) -> {
            wrapper.eq("attr_id", key).or().like("attr_name", key);
          });
    }
    IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);
    return new PageUtils(page);
  }
}
