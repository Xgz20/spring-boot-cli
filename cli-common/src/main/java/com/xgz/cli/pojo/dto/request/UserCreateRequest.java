package com.xgz.cli.pojo.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 创建用户入参
 */
@Data
public class UserCreateRequest {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String nickname;

    private String phone;

    private String email;

    /** 状态：1启用 0禁用 */
    private Integer status;
}
