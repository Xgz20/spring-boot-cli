package com.xgz.cli.framework.mq;

/**
 * Unified consumer callback.
 */
@FunctionalInterface
public interface MqConsumer {

    void onMessage(String topic, String payload);
}
