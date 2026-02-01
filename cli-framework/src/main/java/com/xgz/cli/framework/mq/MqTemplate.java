package com.xgz.cli.framework.mq;

import com.xgz.cli.framework.mq.properties.MqProperties;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Facade for sending messages.
 */
public class MqTemplate {

    private final MqProperties properties;
    private final List<MqClient> clients;

    public MqTemplate(MqProperties properties, List<MqClient> clients) {
        this.properties = properties;
        this.clients = clients;
    }

    public MqType activeType() {
        return properties.getType();
    }

    public void send(String topic, String payload) {
        selectClient().send(topic, payload);
    }

    public void send(String topic, @Nullable String tag, String payload) {
        selectClient().send(topic, tag, payload);
    }

    public void send(String topic, @Nullable String key, @Nullable String tag, String payload, @Nullable Map<String, Object> headers) {
        selectClient().send(topic, key, tag, payload, headers);
    }

    private MqClient selectClient() {
        MqType type = properties.getType();
        return clients.stream()
                .filter(c -> c.type() == type)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("MQ enabled but no client bean found for type=" + type));
    }
}
