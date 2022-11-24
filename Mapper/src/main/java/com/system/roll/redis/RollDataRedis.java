package com.system.roll.redis;

import com.system.roll.entity.pojo.RollData;
import com.system.roll.entity.vo.roll.SingleRollStatisticVo;

import java.util.List;

/**
 * 考勤数据的redis工具类
 * */
public interface RollDataRedis {
    /*生成key的方法*/
    default String rollDataList(String courseId){
        return "rollDataList:"+courseId;
    }

    default String RollDataStatistics(){
        return "RollDataStatistics";
    }

    /**
     * 保存考勤数据列表
     * */
    void saveRollDataList(String courseId, List<RollData> rollDataList);

    /**
     * 判断考勤数据列表是否存在
     * */
    boolean listIsExist(String courseId);

    /**
     * 获取考勤数据列表
     * */
    List<RollData> getRollDataList(String courseId);

    /**
     * 保存考勤数据统计
     * */
    void saveRollDataStatistics(String courseId, SingleRollStatisticVo statistic);

    /*获取考勤数据统计*/
    SingleRollStatisticVo getRollDataStatistics(String courseId);

}
