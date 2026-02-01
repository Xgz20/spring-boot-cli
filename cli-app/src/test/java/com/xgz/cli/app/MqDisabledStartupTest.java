package com.xgz.cli.app;

import com.xgz.cli.framework.mq.MqTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "mq.enabled=false",
        // even if RocketMQ address is wrong, disabled mode shouldn't fail startup
        "rocketmq.name-server=127.0.0.1:1",
        // keep other infra off to make test stable
        "spring.redis.enable=false",
        "sa-token.enable=false",
        "spring.flyway.enabled=false",
        "spring.datasource.dynamic.enabled=false",
        "spring.main.allow-bean-definition-overriding=true"
})
class MqDisabledStartupTest {

    @Autowired
    private MqTemplate mqTemplate;

    @Test
    void contextLoads_andMqTemplateIsPresent() {
        assertThat(mqTemplate).isNotNull();
        mqTemplate.send("any-topic", "hello");
    }
}
