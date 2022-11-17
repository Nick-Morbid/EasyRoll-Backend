package com.system.roll.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义的log工具类
 * */
@Slf4j
@Component(value = "LoggerUtil")
@SuppressWarnings("all")
public class LoggerUtil {
    /*用来保存log内容*/
    private final Map<Long, List<String>> logInfo = new ConcurrentHashMap<>();

    private List<String> getContents(){
        if (!logInfo.containsKey(Thread.currentThread().getId())) logInfo.put(Thread.currentThread().getId(),new ArrayList<>());
        return logInfo.get(Thread.currentThread().getId());
    }

    /**
     * 获取具体的操作日志
     * */
    public String getOperationLog(){
        List<String> contents = getContents();
        logInfo.remove(Thread.currentThread().getId());
        StringBuilder operationLogBuilder = new  StringBuilder();
        for (int i = 0; i < contents.size(); i++) {
            operationLogBuilder.append("(").append(i).append(")").append(contents.get(i+1)).append("\\n");
        }
        return operationLogBuilder.toString();
    }

    /*对log的日志打印方法进行装饰，加上保存到内存中的功能*/

    public void info(String content){
        getContents().add(content);
        log.info(content);
    }

    public void warn(String content){
        getContents().add(content);
        log.warn(content);
    }

    public void error(String content){
        getContents().add(content);
        log.error(content);
    }

    public void debug(String content){
        getContents().add(content);
        log.debug(content);
    }
}
