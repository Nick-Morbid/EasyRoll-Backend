package com.system.roll.flink.task;

import com.system.roll.entity.pojo.RollData;
import com.system.roll.flink.sink.RollDataSink;
import com.system.roll.flink.source.RabbitSource;
import com.system.roll.entity.properites.RabbitProperties;
import com.system.roll.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 定义考勤数据的计算任务
 * */
@Slf4j
@Component(value = "RollDataTask")
public class RollDataTask implements FlinkTask{
    /*执行环境*/
    private final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    @Override
    public void defineTask() {
        /*设置并行度*/
        env.setParallelism(1);
        /*定义数据源*/
        RabbitProperties rabbitProperties = SpringContextUtil.getBean("RabbitProperties");//由于RollDataTask也是通过SpringContextUtil获取的，所以内部的注解注释无效，内部用到的属性也需要用SpringContextUtil来获取
        RabbitSource rabbitSource = new RabbitSource(rabbitProperties.getRollDataQueuePrefix() + "flink", rabbitProperties.getWsExchange(), rabbitProperties.getRollDataSource());
        DataStream<RollData> dataSource = env.addSource(rabbitSource).map(new MyMapFunction());
        /*获取sink处理器*/
        RollDataSink rollDataSink = SpringContextUtil.getBean("RollDataSink");
        dataSource.addSink(rollDataSink);

    }

    @Override
    public void execute() throws Exception {
        env.execute();
    }

    public static class MyMapFunction implements MapFunction<String, RollData>{
        @Override
        public RollData map(String s) {
            String[] split = s.split(",");
            /*如果长度为2，说明传入了课程号和总人数信息*/
            if (split.length==2){
                log.info("收到了课程号为：{}的点名请求，点名人数为：{}",split[0],split[1]);
                return new RollData().setCourseId(split[0]).setEnrollNum(Integer.valueOf(split[1])).setTime(new Timestamp(System.currentTimeMillis()));
            }else if (split.length==3){
                log.info("在课程号：{}的课程中，学号：{}的同学的考勤状态为：{}",split[0],split[1],split[2]);
                return new RollData().setCourseId(split[0]).setStudentId(split[1]).setState(Integer.valueOf(split[2])).setTime(new Timestamp(System.currentTimeMillis()));
            }
            return null;
        }
    }
}
