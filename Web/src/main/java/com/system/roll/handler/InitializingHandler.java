package com.system.roll.handler;

import com.system.roll.context.common.CommonContext;
import com.system.roll.entity.properites.RabbitProperties;
import com.system.roll.rabbit.utils.RabbitUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
    @Resource
    private CommonContext commonContext;

    public void init() throws IOException {
        /*创建接收考勤数据的交换机*/
        rabbitUtil.createExchange(rabbitProperties.getWsExchange());
        log.info("[Thread:{}]:交换机:{}创建完成.",Thread.currentThread().getId(),rabbitProperties.getWsExchange());
        /*创建推送考勤数据的交换机*/
        rabbitUtil.createExchange(rabbitProperties.getRollDataExchange());
        log.info("[Thread:{}]:交换机:{}创建完成.",Thread.currentThread().getId(),rabbitProperties.getRollDataExchange());
        /*创建文件目录*/
        File resourceDir = new File(commonContext.getResourceDir());
        if (!resourceDir.exists()) {
            /*创建资源目录*/
            Files.createDirectory(resourceDir.toPath());
            log.info("[Thread:{}]:文件目录:{}创建完成.",Thread.currentThread().getId(),resourceDir.toString());
        }
        File tempDir = new File(commonContext.getTempDir());
        if (!tempDir.exists()){
            /*创建临时文件目录*/
            Files.createDirectory(tempDir.toPath());
            log.info("[Thread:{}]:文件目录:{}创建完成.",Thread.currentThread().getId(),tempDir.toString());
        }

    }
}
