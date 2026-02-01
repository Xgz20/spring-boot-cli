package com.xgz.cli.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xgz.cli.dao.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * user 表 Mapper
 *
 * @author: Xgz
 * @date: 2026/2/1
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
