package com.system.roll.redis.impl;

import com.system.roll.redis.CourseRedis;
import com.system.roll.redis.uitls.RedisUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component(value = "CourseRedis")
public class CourseRedisImpl implements CourseRedis {
    @Resource
    private RedisUtil redisUtil;
    @Override
    public void saveCourseName(String courseId, String courseName) {
        redisUtil.hset(CourseName(),courseId,courseName);
    }

    @Override
    public String getCourseName(String courseId) {
        return (String) redisUtil.hget(CourseName(),courseId);
    }

    @Override
    public void deleteCourseName(String courseId) {
        redisUtil.hdel(CourseName(),courseId);
    }
}
