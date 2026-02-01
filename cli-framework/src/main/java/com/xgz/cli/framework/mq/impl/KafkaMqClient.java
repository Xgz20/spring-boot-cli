package com.xgz.cli.framework.mq.impl;

import com.xgz.cli.framework.mq.MqClient;
import com.xgz.cli.framework.mq.MqSendException;
import com.xgz.cli.framework.mq.MqType;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Kafka implementation.
 */
public class KafkaMqClient implements MqClient {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaMqClient(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public MqType type() {
        return MqType.KAFKA;
    }

    @Override
    public void send(String topic, String payload) {
        try {
            kafkaTemplate.send(topic, payload);
        } catch (Exception e) {
            throw new MqSendException("Kafka send failed, topic=" + topic, e);
        }
    }

    @Override
    public void send(String topic, String key, String tag, String payload, Map<String, Object> headers) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, payload);
            if (headers != null) {
                headers.forEach((k, v) -> {
                    if (v != null) {
                        record.headers().add(k, String.valueOf(v).getBytes(StandardCharsets.UTF_8));
                    }
                });
            }
            // tag ignored for Kafka
            kafkaTemplate.send(record);
        } catch (Exception e) {
            throw new MqSendException("Kafka send failed, topic=" + topic, e);
        }
    }
}
