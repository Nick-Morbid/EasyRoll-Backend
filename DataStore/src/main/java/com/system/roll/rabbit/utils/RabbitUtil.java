package com.system.roll.rabbit.utils;

import com.system.roll.properites.RabbitProperties;
import com.system.roll.rabbit.context.RabbitContext;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Rabbit工具类
 * */
@Component
public class RabbitUtil {
    @Resource
    private RabbitAdmin rabbitAdmin;
    @Resource
    private RabbitProperties rabbitProperties;
    @Resource
    private RabbitContext rabbitContext;

    /**
     * 创建路由交换机
     * */
    public DirectExchange createExchange(String exchangeName){
        rabbitAdmin.deleteExchange(exchangeName);
        DirectExchange exchange = new DirectExchange(exchangeName,true,false);
        rabbitAdmin.declareExchange(exchange);
        rabbitContext.addExchange(exchangeName,exchange);
        return exchange;
    }


    /**
     * 创建TTL队列
     * @param queueName 队列名称
     * @param exchangeName 交换机名称
     * @param key 路由规则
     * */
    public Queue createTTLQueue(String queueName,String exchangeName,String key){
        rabbitAdmin.deleteQueue(queueName);
        /*队列参数*/
        Map<String,Object> args = new HashMap<>();
        args.put("x-message-ttl",rabbitProperties.getTotalTTL());
        /*创建队列*/
        Queue queue = new Queue(queueName,true,false,false,args);
        /*队列声明*/
        rabbitAdmin.declareQueue(queue);
        /*绑定到指定的交换机上（需要附带上routingKey）*/
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to((DirectExchange) rabbitContext.getExchange(exchangeName)).with(key));
        /*返回队列*/
        return queue;
    }

    /**
     * 删除交换机
     * */
    public void deleteExchange(String name){
        rabbitAdmin.deleteExchange(name);
    }

    /**
     * 删除队列
     * */
    public void deleteQueue(String name){
        rabbitAdmin.deleteQueue(name);
    }

}
