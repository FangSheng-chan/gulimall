package com.ss.gulimall.product.dao;

import com.ss.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author fangsheng
 * @email 445317262@qq.com
 * @date 2021-11-05 15:33:49
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
