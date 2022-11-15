package com.system.roll.flink.task;

import com.system.roll.flink.source.RabbitSource;
import com.system.roll.properites.RabbitProperties;
import com.system.roll.utils.SpringContextUtil;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.stereotype.Component;

/**
 * 定义考勤数据的计算任务
 * */
@Component(value = "RollDataTask")
public class RollDataTask implements FlinkTask{
    /*执行环境*/
    private final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    @Override
    public void defineTask() {
        /*定义数据源*/
        RabbitProperties rabbitProperties = SpringContextUtil.getBean("RabbitProperties");//由于RollDataTask也是通过SpringContextUtil获取的，所以内部的注解注释无效，内部用到的属性也需要用SpringContextUtil来获取
        RabbitSource rabbitSource = new RabbitSource(rabbitProperties.getRollDataQueuePrefix() + "flink", rabbitProperties.getWsExchange(), rabbitProperties.getRollDataSource());
        DataStream<String> dataSource = env.addSource(rabbitSource);
        /*设置并行度*/
        env.setParallelism(8);
        dataSource.print();
    }

    @Override
    public void execute() throws Exception {
        env.execute();
    }
}
