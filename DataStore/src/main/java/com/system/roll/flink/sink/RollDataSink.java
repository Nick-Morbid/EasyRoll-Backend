package com.system.roll.flink.sink;

import com.system.roll.entity.pojo.RollData;
import com.system.roll.flink.context.FlinkContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
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
        /*存入点名人数*/
        if (value.getEnrollNum()!=null){
            log.info("开始对课程号：{}的点名",courseId);
            rollDataContext.add(courseId,value.getEnrollNum());
            return;
        }
        /*存入考勤数据*/
        else {
            rollDataContext.listAdd(courseId,value);
        }
        /*检查是否完成点名*/
        if (Objects.equals(rollDataContext.listLength(courseId), rollDataContext.get(courseId))){
            log.info("课程号：{}完成点名",courseId);
            /*将数据存入redis中*/
            List<RollData> rollDataList = rollDataContext.listGet(courseId);
            rollDataContext.remove(courseId);
            rollDataContext.listRemove(courseId);
        }
    }
}
