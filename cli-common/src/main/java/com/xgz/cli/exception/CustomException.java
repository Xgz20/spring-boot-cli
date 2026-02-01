package com.xgz.cli.exception;

import com.xgz.cli.enums.ResultCode;

/**
 *
 * @author: Xgz
 * @date: 2026/1/31
 */
public class CustomException extends RuntimeException {
    private String code;

    private final String message;

    public CustomException(String message) {
        this.message = message;
    }

    public CustomException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public CustomException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public CustomException(ResultCode resultCode) {
        this.code = String.valueOf(resultCode.getCode());
        this.message = resultCode.getMessage();
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return this.code;
    }
}
