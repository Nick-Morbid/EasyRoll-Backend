package com.system.roll.redis.impl;

import com.system.roll.constant.impl.TimeUnit;
import com.system.roll.entity.pojo.RollData;
import com.system.roll.properites.CommonProperties;
import com.system.roll.redis.RollDataRedis;
import com.system.roll.redis.uitls.RedisUtil;
import com.system.roll.utils.JsonUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component(value = "RollDataRedis")
public class RollDataRedisImpl implements RollDataRedis {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private CommonProperties commonProperties;

    @Override
    public void saveRollDataList(String courseId, List<RollData> rollDataList) {
        redisUtil.set(rollDataList(courseId), JsonUtil.toJson(rollDataList),commonProperties.RollDataExpire(TimeUnit.MINUTE));
    }

    @Override
    public boolean listIsExist(String courseId) {
        return redisUtil.get(rollDataList(courseId))!=null;
    }

    @Override
    @SuppressWarnings("all")
    public List<RollData> getRollDataList(String courseId) {
        if (listIsExist(courseId)) return JsonUtil.toObject((String) redisUtil.get(rollDataList(courseId)),List.class);
        return new ArrayList<>();
    }
}
