package com.xgz.cli.exception;

/**
 *
 * @author: Xgz
 * @date: 2026/1/31
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
