package com.xgz.cli.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author: Xgz
 * @date: 2026/1/31
 */
@Setter
@Getter
@Schema(name = "Result", description = "统一返回结果")
public class Result<T> {
    @Schema(description = "响应码")
    private int code;

    @Schema(description = "响应消息")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    public static final int SUCCESS_CODE = 0;
    public static final int FAIL_CODE = 1;
    public static final String SUCCESS_MSG = "success";

    public Result() {
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS_CODE, SUCCESS_MSG, data);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(FAIL_CODE, message, null);
    }
}
