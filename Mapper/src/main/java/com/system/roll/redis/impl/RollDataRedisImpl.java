package com.system.roll.redis.impl;

import com.google.gson.reflect.TypeToken;
import com.system.roll.entity.constant.impl.TimeUnit;
import com.system.roll.entity.pojo.RollData;
import com.system.roll.entity.properites.CommonProperties;
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
    public List<RollData> getRollDataList(String courseId) {

        if (listIsExist(courseId)) return JsonUtil.getGson().fromJson((String) redisUtil.get(rollDataList(courseId)),new TypeToken<List<RollData>>(){}.getType());
        return new ArrayList<>();
//        return new Gson().fromJson((String) redisUtil.get(rollDataList(courseId)),new TypeToken<List<RollData>>(){}.getType());
    }
}
