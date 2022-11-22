package com.system.roll.redis.impl;

import com.system.roll.redis.StudentRedis;
import com.system.roll.redis.uitls.RedisUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component(value = "StudentRedis")
public class StudentRedisImpl implements StudentRedis {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void savePinYin(String studentId, String[] pinYin) {
        redisUtil.hset(PinYin(),studentId,arrayToString(pinYin));
    }

    @Override
    public String[] getPinYin(String studentId) {
        Object res = redisUtil.hget(PinYin(),studentId);
        if (res==null) return new String[]{};
        return stringToArray(String.valueOf(res));
    }

    @Override
    public void saveName(String studentId, String studentName) {
        redisUtil.hset(StudentName(),studentId,studentName);
    }

    @Override
    public String getName(String studentId) {
        return (String) redisUtil.hget(StudentName(),studentId);
    }

    private String arrayToString(String [] array){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            stringBuilder.append(array[i]);
            if (i!=array.length-1) stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }

    private String[] stringToArray(String str){
        return str.split(",");
    }
}
