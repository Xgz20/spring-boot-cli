package com.xgz.cli.framework.mq.properties;

import com.xgz.cli.framework.mq.MqType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * App-level MQ switch.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "mq")
public class MqProperties {

    /** Enable MQ integration as a whole. */
    @Setter
    private boolean enabled = false;

    /** Chosen middleware. */
    @Setter
    private MqType type = MqType.ROCKETMQ;

    private final Rocketmq rocketmq = new Rocketmq();
    private final Kafka kafka = new Kafka();

    @Setter
    @Getter
    public static class Rocketmq {
        /** Enable RocketMQ specifically. */
        private boolean enabled = false;

    }

    @Setter
    @Getter
    public static class Kafka {
        /** Enable Kafka specifically. */
        private boolean enabled = false;

    }
}
