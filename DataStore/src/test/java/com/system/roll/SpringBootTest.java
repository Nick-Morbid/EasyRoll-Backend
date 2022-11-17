package com.system.roll;

import com.system.roll.redis.uitls.RedisUtil;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

@org.springframework.boot.test.context.SpringBootTest
public class SpringBootTest {
    @Resource
    private RedisUtil redisUtil;
    @Test
    public void test01(){
        redisUtil.set("k2","v2");
    }
}
