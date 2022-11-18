package com.system.roll.describer;

import com.system.roll.describer.impl.*;
import com.system.roll.redis.RollDataRedis;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 督导队员相关操作的备份器的配置类
 * */
@Configuration
public class DescriberConfig {

    @Resource(name = "DescriberPoll")
    private DescriberPoll describerPoll;
    @Resource(name = "RollDataRedis")
    private RollDataRedis rollDataRedis;

    @Bean(name = "UploadCourseDescriber")
    @ConditionalOnBean(DescriberPoll.class)
    public Describer uploadCourseDescriber(){
        UploadCourseDescriber uploadCourseDescriber = new UploadCourseDescriber();
        uploadCourseDescriber.setRollDataRedis(rollDataRedis);
        describerPoll.addDescriber(uploadCourseDescriber);
        return uploadCourseDescriber;
    }

    @Bean(name = "UpdateCourseDescriber")
    @ConditionalOnBean(DescriberPoll.class)
    public Describer updateCourseDescriber(){
        UpdateCourseDescriber updateCourseDescriber = new UpdateCourseDescriber();
        updateCourseDescriber.setRollDataRedis(rollDataRedis);
        describerPoll.addDescriber(updateCourseDescriber);
        return updateCourseDescriber;
    }

    @Bean(name = "DeleteCourseDescriber")
    @ConditionalOnBean(DescriberPoll.class)
    public Describer deleteCourseDescriber(){
        DeleteCourseDescriber deleteCourseDescriber = new DeleteCourseDescriber();
        deleteCourseDescriber.setRollDataRedis(rollDataRedis);
        describerPoll.addDescriber(deleteCourseDescriber);
        return deleteCourseDescriber;
    }

    @Bean(name = "RollTakingDescriber")
    @ConditionalOnBean(DescriberPoll.class)
    public Describer rollTakingDescriber(){
        RollTakingDescriber rollTakingDescriber = new RollTakingDescriber();
        rollTakingDescriber.setRollDataRedis(rollDataRedis);
        describerPoll.addDescriber(rollTakingDescriber);
        return rollTakingDescriber;
    }

    @Bean(name = "LeaveSolvingDescriber")
    @ConditionalOnBean(DescriberPoll.class)
    public Describer leaveSolvingDescriber(){
        LeaveSolvingDescriber leaveSolvingDescriber = new LeaveSolvingDescriber();
        leaveSolvingDescriber.setRollDataRedis(rollDataRedis);
        describerPoll.addDescriber(leaveSolvingDescriber);
        return leaveSolvingDescriber;
    }

}
