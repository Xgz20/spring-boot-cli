package com.xgz.cli.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(name = "LoginResponse", description = "登录响应")
@Data
@AllArgsConstructor
public class LoginResponse {

    @Schema(description = "token 名称（header 名）")
    private String tokenName;

    @Schema(description = "token 值")
    private String tokenValue;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;
}
