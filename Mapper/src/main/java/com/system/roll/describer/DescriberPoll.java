package com.system.roll.describer;

import com.system.roll.entity.constant.impl.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 备份器池
 * */
@Slf4j
@Component(value = "DescriberPoll")
public class DescriberPoll {
    private final Map<OperationType,Describer> describerMap = new ConcurrentHashMap<>();

    public Describer getDescriber(OperationType operationType){
        return describerMap.get(operationType);
    }

    public void addDescriber(Describer describer){
        log.info("注入备用器：{}，处理的操作类型为：[{}]",describer.getClass(),describer.getOperationType());
        describerMap.put(describer.getOperationType(),describer);
    }
}
