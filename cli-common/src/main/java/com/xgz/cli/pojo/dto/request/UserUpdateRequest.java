package com.xgz.cli.pojo.dto.request;

import lombok.Data;

/**
 * 更新用户入参
 */
@Data
public class UserUpdateRequest {

    /** 密码（明文）：传则更新，不传不更新 */
    private String password;

    private String nickname;

    private String phone;

    private String email;

    /** 状态：1启用 0禁用 */
    private Integer status;
}
