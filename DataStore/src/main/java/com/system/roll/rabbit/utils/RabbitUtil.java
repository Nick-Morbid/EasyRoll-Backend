package com.system.roll.rabbit.utils;

import com.system.roll.properites.RabbitProperties;
import com.system.roll.rabbit.context.RabbitContext;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Rabbit工具类
 * */
@Component(value = "RabbitUtil")
public class RabbitUtil {
    @Resource
    private RabbitAdmin rabbitAdmin;
    @Resource
    private RabbitProperties rabbitProperties;
    @Resource
    private RabbitContext rabbitContext;

    /**
     * 创建路由交换机
     * @param exchangeName 交换机的名称
     * */
    public DirectExchange createExchange(String exchangeName){
        rabbitAdmin.deleteExchange(exchangeName);
        /*创建交换机*/
        DirectExchange exchange = new DirectExchange(exchangeName,true,false);
        /*声明交换机*/
        rabbitAdmin.declareExchange(exchange);
        /*保存到上下文对象中*/
        rabbitContext.addExchange(exchangeName,exchange);
        return exchange;
    }

    /**
     * 创建有参队列
     * @param queueName 队列名称
     * @param exchangeName 交换机名称
     * @param key 路由规则
     * @param args 参数
     * */
    public Queue createQueue(String queueName,String exchangeName,String key,Map<String,Object> args){
        rabbitAdmin.deleteQueue(queueName);
        /*创建队列*/
        Queue queue = new Queue(queueName,true,false,false,args);
        /*队列声明*/
        rabbitAdmin.declareQueue(queue);
        /*绑定到指定的交换机上（需要附带上routingKey）*/
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to((DirectExchange) rabbitContext.getExchange(exchangeName)).with(key));
        /*保存到上下文对象中*/
        rabbitContext.addQueue(queueName,queue);
        /*返回队列*/
        return queue;
    }

    /**
     * 创建无参队列
     * @param queueName 队列名称
     * @param exchangeName 交换机名称
     * @param key 路由规则
     * */
    public Queue createQueue(String queueName,String exchangeName,String key){
        return createQueue(queueName,exchangeName,key,null);
    }

    /**
     * 创建TTL队列
     * @param queueName 队列名称
     * @param exchangeName 交换机名称
     * @param key 路由规则
     * */
    public Queue createTTLQueue(String queueName,String exchangeName,String key){
        /*队列参数*/
        Map<String,Object> args = new HashMap<>();
        args.put("x-message-ttl",rabbitProperties.getTotalTTL());
        return createQueue(queueName,exchangeName,key,args);
    }

    /**
     * 删除交换机
     * */
    public void deleteExchange(String name){
        rabbitContext.removeExchange(name);
        rabbitAdmin.deleteExchange(name);
    }

    /**
     * 删除队列
     * */
    public void deleteQueue(String name){
        rabbitContext.removeQueue(name);
        rabbitAdmin.deleteQueue(name);
    }

    /**
     * 消费一条数据
     * @param queueName 队列名称
     * @param timeLimit 等待时限
     * */
    public String consume(String queueName,long timeLimit){
        Message message = rabbitAdmin.getRabbitTemplate().receive(queueName,timeLimit);
        if (message!=null) return new String(message.getBody(), StandardCharsets.UTF_8);
        else return null;
    }

}
