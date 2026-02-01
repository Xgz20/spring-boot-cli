package com.xgz.cli.enums;

import lombok.Getter;

/**
 *
 * @author: Xgz
 * @date: 2026/1/31
 */
@Getter
public enum ResultCode {

    /**
     * 通用返回码定义
     */
    SUCCESS(200, "操作成功"),

    BAD_REQUEST(400, "参数异常"),

    TOKEN_FAILURE(401, "登录失效，请重新登录"),

    ACCOUNT_OR_PASSWORD_ERROR(402, "用户名或密码错误"),

    CHECK_TOKEN_FAIL(403, "TOKEN 检验失败"),

    REQUEST_METHOD_ERROR(407, "请求方式错误，请确认API请求方式GET/POST/PUT/DELETE"),

    SERVER_ERROR(500, "系统错误"),

    // 请求用户中心错误
    REQUEST_USER_SERVER_ERROR(400001, "请求用户中心服务失败！"),

    PLUGIN_TOKEN_VALID_ERROR(400002, "插件token校验失败"),

    USER_TOKEN_VALID_ERROR(400003, "用户中心token校验失败"),

    ACCOUNT_NOT_EXIST(400003, "账号不存在，请先注册或联系管理员创建"),

    NO_PERMISSION_ERROR(400004, "当前帐号无操作权限"),

    USER_CENTER_GET_USER_EMPTY(400005, "当前用户不存在"),

    REQUEST_TEST_OPS_SERVER_ERROR(400006, "测试助手运营平台请求失败"),

    REQUEST_TEST_AGENT_SERVER_ERROR(400007, "agent服务请求失败"),

    ;

    private final int code;

    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
