package com.xuecheng.auth;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * RedisTest
 *
 * @author guoxing
 * @date 10/23/2019 10:31 AM
 * @since 2.0.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Test
    public void testRedis() {
        String key = "test";
        String value = "test";
        stringRedisTemplate.opsForValue().set(key, value);
        String s = stringRedisTemplate.opsForValue().get(key);
        log.info(s);
        stringRedisTemplate.delete(key);
    }
}
