package com.ss.gulimall.order.dao;

import com.ss.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author fangsheng
 * @email 445317262@qq.com
 * @date 2021-10-17 15:33:44
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
