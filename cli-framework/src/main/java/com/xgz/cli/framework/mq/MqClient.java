package com.xgz.cli.framework.mq;

import java.util.Map;

/**
 * Unified MQ client abstraction.
 *
 * Contract:
 * - send(String topic, String payload): send a plain text message
 * - send(String topic, String tag, String payload): send with tag (RocketMQ supports it; Kafka will ignore tag)
 * - send(String topic, String key, String tag, String payload, Map<String,Object> headers): advanced variant
 */
public interface MqClient {

    MqType type();

    void send(String topic, String payload);

    default void send(String topic, String tag, String payload) {
        send(topic, null, tag, payload, null);
    }

    default void send(String topic, String key, String tag, String payload, Map<String, Object> headers) {
        // minimal impl for old callers
        send(topic, payload);
    }
}
