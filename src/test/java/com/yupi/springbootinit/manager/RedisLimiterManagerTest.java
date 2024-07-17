package com.yupi.springbootinit.manager;

import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisLimiterManagerTest {

    @Resource
    private RedisLimiterManager redisLimiterManager;

    @Test
    void doRateLimit() throws InterruptedException {
        String userId = "1";
        for (int i = 0; i < 2; i++) {
            redisLimiterManager.doRateLimit(userId);
            System.out.println("成功");
        }
        Thread.sleep(1000);
        for (int i = 0; i < 5; i++) {
            redisLimiterManager.doRateLimit(userId);
            System.out.println("成功");
        }
    }

    @Test
    void connectToRedis() {
        Config config = new Config();
        config.useSingleServer()
                .setDatabase(1)
                .setAddress("redis://122.51.87.16:6379")
                .setPassword("123456");

        RedissonClient redisson = Redisson.create(config);
        assertNotNull(redisson);
    }

    @Test
    void sayHello() {
        System.out.println("Hello, RedisLimiterManagerTest!");
    }
}