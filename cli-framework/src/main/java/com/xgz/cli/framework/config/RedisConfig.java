package com.xgz.cli.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

/**
 *
 * @author: Xgz
 * @date: 2026/1/31
 */
@Configuration
// 如果spring.redis.enable=false，则不会加载该配置类
@ConditionalOnProperty(value = "spring.redis.enable", havingValue = "true", matchIfMissing = true)
public class RedisConfig {
    @Autowired
    private RedisProperties redisProperties;

    @Value("${spring.redis.mode}")
    private String redisMode;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value("${spring.redis.database}")
    private int database;

    @Value("${spring.redis.sentinel.master}")
    private String sentinelMaster;

    private static final String STANDALONE_MODE = "single";
    private static final String SENTINEL_MODE = "sentinel";
    private static final String CLUSTER_MODE = "cluster";

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        switch (redisMode.toLowerCase()) {
            case SENTINEL_MODE:
                return createSentinelConnectionFactory();
            case CLUSTER_MODE:
                return createClusterConnectionFactory();
            case STANDALONE_MODE:
            default:
                return createStandaloneConnectionFactory();
        }
    }

    private RedisConnectionFactory createStandaloneConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisHost);
        configuration.setPort(redisPort);
        configuration.setPassword(RedisPassword.of(redisPassword));
        configuration.setDatabase(database);
        return new LettuceConnectionFactory(configuration);
    }

    private RedisConnectionFactory createSentinelConnectionFactory() {
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master(sentinelMaster);
        List<String> sentinelNodes = redisProperties.getSentinel().getNodes();
        for (String node : sentinelNodes) {
            String[] parts = node.split(":");
            sentinelConfig.sentinel(parts[0], Integer.parseInt(parts[1]));
        }
        sentinelConfig.setPassword(RedisPassword.of(redisPassword));
        sentinelConfig.setDatabase(database);
        return new LettuceConnectionFactory(sentinelConfig);
    }

    private RedisConnectionFactory createClusterConnectionFactory() {
        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();
        List<String> clusterNodes = redisProperties.getCluster().getNodes();
        for (String node : clusterNodes) {
            String[] parts = node.split(":");
            clusterConfig.clusterNode(parts[0], Integer.parseInt(parts[1]));
        }
        clusterConfig.setPassword(RedisPassword.of(redisPassword));
        return new LettuceConnectionFactory(clusterConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用 String 序列化器处理 KEY
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // 使用 JSON 序列化器处理 VALUE
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }
}
