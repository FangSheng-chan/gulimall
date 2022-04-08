package com.ss.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ss.common.utils.PageUtils;
import com.ss.gulimall.product.entity.AttrEntity;
import com.ss.gulimall.product.vo.AttrRespVo;
import com.ss.gulimall.product.vo.AttrVo;

import java.util.Map;

/**
 * 商品属性
 *
 * @author fangsheng
 * @email 445317262@qq.com
 * @date 2021-11-05 15:33:49
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId);

    AttrRespVo getInfo(Long attrId);
}

