package com.system.roll.handler;

import com.system.roll.properites.RabbitProperties;
import com.system.roll.rabbit.utils.RabbitUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 项目启动后的初始化操作
 * */
@Slf4j
@Data
@Component(value = "InitializingHandler")
public class InitializingHandler {

    @Resource
    private RabbitUtil rabbitUtil;
    @Resource
    private RabbitProperties rabbitProperties;

    public void init() {
        /*创建发送考勤数据给数据仓的交换机*/
        rabbitUtil.createExchange(rabbitProperties.getRollDataExchange());
        log.info("[Thread:{}]:交换机:{}创建完成.",Thread.currentThread().getId(),rabbitProperties.getRollDataExchange());
    }
}
