package com.xgz.cli.framework.mq.config;

import com.xgz.cli.framework.mq.MqClient;
import com.xgz.cli.framework.mq.MqTemplate;
import com.xgz.cli.framework.mq.impl.KafkaMqClient;
import com.xgz.cli.framework.mq.impl.NoopMqClient;
import com.xgz.cli.framework.mq.impl.RocketMqClient;
import com.xgz.cli.framework.mq.properties.MqProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Collections;

/**
 * MQ auto config:
 * - mq.enabled=false => provides a Noop MqTemplate (safe even if RocketMQ/Kafka configs are wrong)
 * - mq.enabled=true  => creates MqClient according to mq.type and per-middleware enable flags
 */
@Configuration
@EnableConfigurationProperties(MqProperties.class)
public class MqAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "mq", name = "enabled", havingValue = "false", matchIfMissing = true)
    public MqTemplate noopMqTemplate(MqProperties properties) {
        // Provide a sendable template which just drops messages.
        NoopMqClient client = new NoopMqClient(properties.getType());
        return new MqTemplate(properties, Collections.singletonList(client));
    }

    @Bean
    @ConditionalOnProperty(prefix = "mq", name = "enabled", havingValue = "true")
    @ConditionalOnMissingBean
    public MqTemplate mqTemplate(MqProperties properties, java.util.List<MqClient> clients) {
        return new MqTemplate(properties, clients);
    }

    @Configuration
    @ConditionalOnExpression("'true'.equalsIgnoreCase('${mq.enabled:false}') and 'true'.equalsIgnoreCase('${mq.rocketmq.enabled:false}') and 'ROCKETMQ'.equalsIgnoreCase('${mq.type:ROCKETMQ}')")
    @ConditionalOnClass(RocketMQTemplate.class)
    static class RocketMqClientConfiguration {
        @Bean
        public MqClient rocketMqClient(RocketMQTemplate rocketMQTemplate) {
            return new RocketMqClient(rocketMQTemplate);
        }
    }

    @Configuration
    @AutoConfigureAfter(KafkaAutoConfiguration.class)
    @ConditionalOnExpression("'true'.equalsIgnoreCase('${mq.enabled:false}') and 'true'.equalsIgnoreCase('${mq.kafka.enabled:false}') and 'KAFKA'.equalsIgnoreCase('${mq.type:ROCKETMQ}')")
    @ConditionalOnClass(KafkaTemplate.class)
    static class KafkaMqClientConfiguration {
        @Bean
        public MqClient kafkaMqClient(KafkaTemplate<String, String> kafkaTemplate) {
            return new KafkaMqClient(kafkaTemplate);
        }
    }
}
