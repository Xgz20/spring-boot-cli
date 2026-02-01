package com.xgz.cli.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xgz.cli.app.service.UserService;
import com.xgz.cli.dao.entity.UserEntity;
import com.xgz.cli.pojo.Result;
import com.xgz.cli.pojo.dto.request.UserCreateRequest;
import com.xgz.cli.pojo.dto.request.UserUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 *
 * @author: Xgz
 * @date: 2026/1/31
 */
@Tag(name = "UserController", description = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "hello接口")
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Hello, World!");
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public Result<Long> create(@RequestBody @Valid UserCreateRequest req) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(req.getUsername());
        userEntity.setPassword(passwordEncoder.encode(req.getPassword()));
        userEntity.setNickname(req.getNickname());
        userEntity.setPhone(req.getPhone());
        userEntity.setEmail(req.getEmail());
        userEntity.setStatus(req.getStatus() == null ? 1 : req.getStatus());

        userService.save(userEntity);
        return Result.success(userEntity.getId());
    }

    @Operation(summary = "根据ID删除用户")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(userService.removeById(id));
    }

    @Operation(summary = "根据ID更新用户")
    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody UserUpdateRequest req) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        if (req.getPassword() != null && !req.getPassword().trim().isEmpty()) {
            userEntity.setPassword(passwordEncoder.encode(req.getPassword()));
        }
        userEntity.setNickname(req.getNickname());
        userEntity.setPhone(req.getPhone());
        userEntity.setEmail(req.getEmail());
        userEntity.setStatus(req.getStatus());

        return Result.success(userService.updateById(userEntity));
    }

    @Operation(summary = "根据ID查询用户")
    @GetMapping("/{id}")
    public Result<UserEntity> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @Operation(summary = "分页查询用户")
    @GetMapping("/page")
    public Result<Page<UserEntity>> page(@RequestParam(defaultValue = "1") long pageNum,
                                         @RequestParam(defaultValue = "10") long pageSize,
                                         @RequestParam(required = false) String username) {

        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        if (username != null && !username.trim().isEmpty()) {
            wrapper.like(UserEntity::getUsername, username.trim());
        }
        wrapper.orderByDesc(UserEntity::getId);

        Page<UserEntity> page = userService.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(page);
    }
}
