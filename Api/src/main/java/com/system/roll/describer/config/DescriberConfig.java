package com.system.roll.describer.config;

import com.system.roll.describer.Describer;
import com.system.roll.describer.DescriberPoll;
import com.system.roll.describer.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 备份器的配置类
 * */
@Configuration
public class DescriberConfig {

    @Resource(name = "DescriberPoll")
    private DescriberPoll describerPoll;

    @Bean(name = "UploadCourseDescriber")
    @ConditionalOnBean(DescriberPoll.class)
    public Describer uploadCourseDescriber(){
        Describer uploadCourseDescriber = new UploadCourseDescriber();
        describerPoll.addDescriber(uploadCourseDescriber);
        return uploadCourseDescriber;
    }

    @Bean(name = "UpdateCourseDescriber")
    @ConditionalOnBean(DescriberPoll.class)
    public Describer updateCourseDescriber(){
        Describer updateCourseDescriber = new UpdateCourseDescriber();
        describerPoll.addDescriber(updateCourseDescriber);
        return updateCourseDescriber;
    }

    @Bean(name = "DeleteCourseDescriber")
    @ConditionalOnBean(DescriberPoll.class)
    public Describer deleteCourseDescriber(){
        Describer deleteCourseDescriber = new DeleteCourseDescriber();
        describerPoll.addDescriber(deleteCourseDescriber);
        return deleteCourseDescriber;
    }

    @Bean(name = "RollTakingDescriber")
    @ConditionalOnBean(DescriberPoll.class)
    public Describer rollTakingDescriber(){
        Describer rollTakingDescriber = new RollTakingDescriber();
        describerPoll.addDescriber(rollTakingDescriber);
        return rollTakingDescriber;
    }

    @Bean(name = "LeaveSolvingDescriber")
    @ConditionalOnBean(DescriberPoll.class)
    public Describer leaveSolvingDescriber(){
        Describer leaveSolvingDescriber = new LeaveSolvingDescriber();
        describerPoll.addDescriber(leaveSolvingDescriber);
        return leaveSolvingDescriber;
    }

}
