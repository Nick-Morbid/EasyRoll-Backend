package com.system.roll.flink.context;

import com.system.roll.entity.pojo.RollData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 上下文对象的配置类
 * */
@Configuration
public class ContextConfig {

    /**
     * 定义存储RollData类型数据的上下文对象
     * 上下文中的listMap存放考勤数据
     * 上下文中的map存放点名课程的总人数
     * */
    @Bean(name = "RollDataContext")
    public FlinkContext<RollData,Integer> rollDataContext(){
        return new FlinkContext<>();
    }
}
