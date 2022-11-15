package com.system.roll.handler;

import com.system.roll.flink.task.FlinkTask;
import com.system.roll.properites.RabbitProperties;
import com.system.roll.rabbit.utils.RabbitUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目启动后的初始化操作
 * */
@Slf4j
@Data
@Component(value = "InitializingHandler")
@ConfigurationProperties(prefix = "custom")
public class InitializingHandler {

    private List<String> taskClassNames;

    private List<FlinkTask> tasks = new ArrayList<>();

    @Resource
    private RabbitUtil rabbitUtil;
    @Resource
    private RabbitProperties rabbitProperties;

    public void init() throws Exception {
        /*创建推送考勤数据的交换机*/
        rabbitUtil.createExchange(rabbitProperties.getRollDataExchange());
        log.info("[Thread:{}]:交换机:{}创建完成.",Thread.currentThread().getId(),rabbitProperties.getRollDataExchange());
        /*执行所有的task*/
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        for (String taskClassName : taskClassNames) {
            FlinkTask task = (FlinkTask) classLoader.loadClass(taskClassName).getDeclaredConstructor().newInstance();
            /*定义任务的内容*/
            task.defineTask();
            /*执行任务*/
            task.execute();
            log.info("[Thread:{}]{}所定义的任务开始执行.",Thread.currentThread().getId(),taskClassName);
        }


    }
}
