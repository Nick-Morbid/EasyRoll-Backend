package com.system.roll.redis;

import com.system.roll.entity.pojo.RollData;

import java.util.List;

/**
 * 考勤数据的redis工具类
 * */
public interface RollDataRedis {
    /*生成key的方法*/
    default String rollDataList(String courseId){
        return "rollDataList:"+courseId;
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

}
