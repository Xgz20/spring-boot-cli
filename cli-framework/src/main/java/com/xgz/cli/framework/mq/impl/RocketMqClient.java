package com.xgz.cli.framework.mq.impl;

import com.xgz.cli.framework.mq.MqClient;
import com.xgz.cli.framework.mq.MqSendException;
import com.xgz.cli.framework.mq.MqType;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

import java.util.Map;

/**
 * RocketMQ implementation.
 */
public class RocketMqClient implements MqClient {

    private final RocketMQTemplate rocketMQTemplate;

    public RocketMqClient(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    @Override
    public MqType type() {
        return MqType.ROCKETMQ;
    }

    @Override
    public void send(String topic, String payload) {
        try {
            rocketMQTemplate.convertAndSend(topic, payload);
        } catch (Exception e) {
            throw new MqSendException("RocketMQ send failed, topic=" + topic, e);
        }
    }

    @Override
    public void send(String topic, String key, String tag, String payload, Map<String, Object> headers) {
        String destination = topic;
        if (tag != null && !tag.trim().isEmpty()) {
            destination = topic + ":" + tag;
        }
        try {
            rocketMQTemplate.convertAndSend(destination, payload);
        } catch (Exception e) {
            throw new MqSendException("RocketMQ send failed, destination=" + destination, e);
        }
    }
}
