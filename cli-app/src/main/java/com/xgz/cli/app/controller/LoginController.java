package com.xgz.cli.app.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xgz.cli.app.service.UserService;
import com.xgz.cli.dao.entity.UserEntity;
import com.xgz.cli.exception.CustomException;
import com.xgz.cli.pojo.Result;
import com.xgz.cli.pojo.dto.request.LoginRequest;
import com.xgz.cli.pojo.vo.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 登录接口
 */
@Tag(name = "LoginController", description = "登录相关接口")
@RestController
public class LoginController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public LoginController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "用户登录（Sa-Token + BCrypt）")
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody @Valid LoginRequest req) {
        String username = req.getUsername().trim();
        String password = req.getPassword();

        UserEntity user = userService.getOne(new LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getUsername, username)
                .eq(UserEntity::getStatus, 1)
                .last("LIMIT 1"));

        if (user == null) {
            throw new CustomException("用户名或密码错误");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new CustomException("该账号未设置密码，请联系管理员");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException("用户名或密码错误");
        }

        // 标记登录
        StpUtil.login(user.getId());

        LoginResponse resp = new LoginResponse(
                StpUtil.getTokenName(),
                StpUtil.getTokenValue(),
                user.getId(),
                user.getUsername(),
                user.getNickname());

        return Result.success(resp);
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result<Boolean> logout() {
        StpUtil.logout();
        return Result.success(true);
    }
}
