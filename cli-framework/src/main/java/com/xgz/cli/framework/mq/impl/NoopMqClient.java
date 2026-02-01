package com.xgz.cli.framework.mq.impl;

import com.xgz.cli.framework.mq.MqClient;
import com.xgz.cli.framework.mq.MqType;

/**
 * No-op implementation when MQ integration is disabled.
 */
public class NoopMqClient implements MqClient {

    private final MqType type;

    public NoopMqClient(MqType type) {
        this.type = type;
    }

    @Override
    public MqType type() {
        return type;
    }

    @Override
    public void send(String topic, String payload) {
        // intentionally do nothing
    }
}
