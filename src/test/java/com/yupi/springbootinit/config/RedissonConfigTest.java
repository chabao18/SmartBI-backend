package com.yupi.springbootinit.config;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.*;


class RedissonConfigTest {

    @Test
    void redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setDatabase(0)
                .setAddress("redis://" + "122.51.87.16" + ":" + "6379")
                .setPassword("123456");
        RedissonClient redisson = Redisson.create(config);
        assertNotNull(redisson);
    }
}