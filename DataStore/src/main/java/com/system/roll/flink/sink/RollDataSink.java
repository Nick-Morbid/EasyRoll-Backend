package com.system.roll.flink.sink;

import com.system.roll.entity.constant.impl.RollDataType;
import com.system.roll.entity.constant.impl.TimeUnit;
import com.system.roll.entity.pojo.RollData;
import com.system.roll.entity.properites.CommonProperties;
import com.system.roll.entity.properites.RabbitProperties;
import com.system.roll.flink.context.FlinkContext;
import com.system.roll.rabbit.utils.RabbitUtil;
import com.system.roll.redis.RollDataRedis;
import com.system.roll.utils.JsonUtil;
import com.system.roll.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 考勤数据源的处理器
 * */
@Slf4j
@Component(value = "RollDataSink")
public class RollDataSink extends RichSinkFunction<RollData> {

    @Resource(name = "RollDataContext")
    private FlinkContext<RollData,Integer> rollDataContext;

    @Override
    public void invoke(RollData value, Context context) {
        String courseId = value.getCourseId();
        RabbitUtil rabbitUtil = SpringContextUtil.getBean("RabbitUtil");
        RabbitProperties rabbitProperties = SpringContextUtil.getBean("RabbitProperties");

        RollDataType flag = value.getFlag();

        switch (flag){
            /*判断是否为开始点名*/
            case START_ROLL:
                log.info("开始对课程号：{}的点名",courseId);
                rollDataContext.add(courseId,value.getEnrollNum());
                /*创建对应的消息队列*/
                rabbitUtil.createQueue(rabbitProperties.getRollDataQueuePrefix()+courseId,rabbitProperties.getRollDataExchange(),courseId,false);
                break;
            /*判断是否为点名中断*/
            case ABORT_ROLL:
                log.info("课程：{}的点名中断",courseId);
                break;
            /*判断是否为点名数据*/
            case SINGLE:
                /*存入考勤数据*/
                if (!rollDataContext.listContainKey(courseId)) value.setEnrollNum(rollDataContext.get(courseId));
                rollDataContext.listAdd(courseId,value);
                /*给相应课程的老师推送数据*/
                CommonProperties commonProperties = SpringContextUtil.getBean("CommonProperties");
                rabbitUtil.sendTTLMessage(rabbitProperties.getRollDataExchange(),courseId, JsonUtil.toJson(value),commonProperties.MessageTTL(TimeUnit.MINUTE));
                /*检查是否完成点名*/
                if (Objects.equals(rollDataContext.listLength(courseId), rollDataContext.get(courseId))){
                    log.info("课程号：{}完成点名",courseId);
                    RollDataRedis rollDataRedis = SpringContextUtil.getBean("RollDataRedis");
                    /*将统计结果暂存到redis中*/
                    rollDataRedis.saveRollDataList(courseId,rollDataContext.listGet(courseId));
                    rollDataContext.remove(courseId);
                    rollDataContext.listRemove(courseId);
                }
                break;
        }
    }
}
