package com.ss.gulimall.member.dao;

import com.ss.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author fangsheng
 * @email 445317262@qq.com
 * @date 2021-10-17 15:28:51
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
