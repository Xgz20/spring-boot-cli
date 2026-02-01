package com.xgz.cli.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xgz.cli.app.service.UserService;
import com.xgz.cli.dao.entity.UserEntity;
import com.xgz.cli.dao.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * 用户 Service 实现
 *
 * @author: Xgz
 * @date: 2026/2/1
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
}
