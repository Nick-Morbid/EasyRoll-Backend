package com.system.roll.flink.sink;

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
        /*存入点名人数*/
        if (value.getEnrollNum()!=null){
            log.info("开始对课程号：{}的点名",courseId);
            rollDataContext.add(courseId,value.getEnrollNum());
            /*创建对应的消息队列*/
            CommonProperties commonProperties = SpringContextUtil.getBean("CommonProperties");
            rabbitUtil.createTTLQueue(rabbitProperties.getRollDataQueuePrefix()+courseId,rabbitProperties.getRollDataExchange(),courseId,true,commonProperties.RollDataTTL(TimeUnit.MINUTE));
            return;
        }
        else {
            /*存入考勤数据*/
            if (!rollDataContext.listContainKey(courseId)) value.setEnrollNum(rollDataContext.get(courseId));
            rollDataContext.listAdd(courseId,value);
            /*给相应课程的老师推送数据*/
            rabbitUtil.sendMessage(rabbitProperties.getRollDataExchange(),courseId, JsonUtil.toJson(value));
        }
        /*检查是否完成点名*/
        if (Objects.equals(rollDataContext.listLength(courseId), rollDataContext.get(courseId))){
            log.info("课程号：{}完成点名",courseId);
            RollDataRedis rollDataRedis = SpringContextUtil.getBean("RollDataRedis");
            /*将统计结果暂存到redis中*/
            rollDataRedis.saveRollDataList(courseId,rollDataContext.listGet(courseId));
            rollDataContext.remove(courseId);
            rollDataContext.listRemove(courseId);
        }
    }
}
