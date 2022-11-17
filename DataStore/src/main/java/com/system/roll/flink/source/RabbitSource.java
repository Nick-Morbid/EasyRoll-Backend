package com.system.roll.flink.source;

import com.system.roll.rabbit.utils.RabbitUtil;
import com.system.roll.utils.SpringContextUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

/**
 * 自定义的rabbitMq数据源
 * */
@Data
@EqualsAndHashCode(callSuper = true)
public class RabbitSource extends RichSourceFunction<String> {
    private String queueName;
    private boolean isRunning;

    public RabbitSource(String queueName,String exchangeName,String routingKey){
        /*手动注入组件*/
        RabbitUtil rabbitUtil =SpringContextUtil.getBean("RabbitUtil");
        /*创建队列*/
        rabbitUtil.createQueue(queueName, exchangeName, routingKey,false);
        this.queueName = queueName;
        /*设置标志位为false（还未开始接收消息）*/
        isRunning = false;
    }

    @Override
    public void open(Configuration parameters) {
        this.isRunning = true;
    }

    @Override
    @SuppressWarnings("all")
    public void run(SourceContext<String> sourceContext) throws Exception {
        /*手动注入组件*/
        RabbitUtil rabbitUtil =SpringContextUtil.getBean("RabbitUtil");
        while (isRunning){
            Thread.sleep(900);//等待0.9s
            String data = rabbitUtil.consume(this.queueName, 100);//读0.1s
            if (data==null) continue;
            sourceContext.collect(data);
        }
    }

    @Override
    public void cancel() {
        /*手动注入组件*/
        RabbitUtil rabbitUtil =SpringContextUtil.getBean("RabbitUtil");
        this.isRunning = false;
        rabbitUtil.deleteQueue(this.queueName);
    }

}
