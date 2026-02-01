package com.xgz.cli.framework.mq;

public class MqSendException extends RuntimeException {
    public MqSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
