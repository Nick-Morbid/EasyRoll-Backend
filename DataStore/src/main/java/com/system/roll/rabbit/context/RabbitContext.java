package com.system.roll.rabbit.context;

import com.system.roll.constant.impl.ResultCode;
import com.system.roll.exception.impl.ServiceException;
import org.springframework.amqp.core.Exchange;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rabbit的上下文对象
 * */
@Component
@SuppressWarnings("all")
public class RabbitContext {
    /*
    * 交换机集合
    * */
    private Map<String, Exchange> exchangeMap = new ConcurrentHashMap<>();

    public void addExchange(String name,Exchange exchange){
        this.exchangeMap.put(name,exchange);
    }

    public Exchange getExchange(String name){
        if (exchangeMap.containsKey(name)){
            return exchangeMap.get(name);
        }else {
            throw new ServiceException(ResultCode.NOT_RELATED_RESOURCE);
        }
    }

}
