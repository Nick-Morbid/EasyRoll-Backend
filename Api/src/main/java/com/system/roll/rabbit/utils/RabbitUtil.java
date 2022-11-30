package com.system.roll.rabbit.utils;

import com.system.roll.entity.properites.RabbitProperties;
import com.system.roll.rabbit.context.RabbitContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Rabbit工具类
 * */
@Slf4j
@Component(value = "RabbitUtil")
@SuppressWarnings("all")
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
//        rabbitAdmin.deleteExchange(exchangeName);
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
     * @param deleteExisted 是否删除已经创建的队列
     * */
    public void createQueue(String queueName,String exchangeName,String key,Map<String,Object> args,boolean deleteExisted){
        /*删除原有队列*/
        if (deleteExisted) rabbitAdmin.deleteQueue(queueName);
        else if (rabbitAdmin.getQueueInfo(queueName) != null) return;
        //        rabbitAdmin.deleteQueue(queueName);
        /*创建队列*/
        Queue queue = new Queue(queueName,true,false,false,args);
        /*队列声明*/
        rabbitAdmin.declareQueue(queue);
        /*绑定到指定的交换机上（需要附带上routingKey）*/
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to((DirectExchange) rabbitContext.getExchange(exchangeName)).with(key));
        /*保存到上下文对象中*/
        rabbitContext.addQueue(queueName,queue);
    }

    /**
     * 创建无参队列
     * @param queueName 队列名称
     * @param exchangeName 交换机名称
     * @param key 路由规则
     * @param deleteExisted 是否删除已经创建的队列
     * */
    public void createQueue(String queueName,String exchangeName,String key,boolean deleteExisted){
        createQueue(queueName,exchangeName,key,null,deleteExisted);
    }

    /**
     * 创建TTL队列
     * @param queueName 队列名称
     * @param exchangeName 交换机名称
     * @param key 路由规则
     * @param deleteExisted 是否删除已经创建的队列
     * @param ttl 过期时间
     * */
    public void createTTLQueue(String queueName,String exchangeName,String key,boolean deleteExisted,long ttl){
        /*队列参数*/
        Map<String,Object> args = new HashMap<>();
        args.put("x-message-ttl",ttl);
        createQueue(queueName,exchangeName,key,args,deleteExisted);
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

    /**
     * 发送一条消息
     * */
    public void sendMessage(String exchangeName,String routingKey,String data) {
        log.info("交换机为：{}，路由规则为：{}，发送数据内容为：{}",exchangeName,routingKey,data);
        rabbitAdmin.getRabbitTemplate().convertAndSend(exchangeName,routingKey,data);
    }

    /**
     * 发送一条ttl消息
     * */
    public void sendTTLMessage(String exchangeName,String routingKey,String data,long timeLimit){
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //用这么方法来设置消息的参数！
                message.getMessageProperties().setExpiration(String.valueOf(timeLimit));//这里传入的参数类型为字符串！
                message.getMessageProperties().setContentEncoding("UTF-8");
                return message;
            }
        };
        log.info("交换机为：{}，路由规则为：{}，发送数据内容为：{}，过期时间：{} s",exchangeName,routingKey,data,timeLimit/1000);
        rabbitAdmin.getRabbitTemplate().convertAndSend(exchangeName,routingKey,data,messagePostProcessor);
    }
}
