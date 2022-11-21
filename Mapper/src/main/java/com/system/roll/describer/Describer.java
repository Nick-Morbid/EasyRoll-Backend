package com.system.roll.describer;

import com.system.roll.entity.constant.impl.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * 记录器接口
 * */
public interface Describer {
    /**
     * 保存快照
     * @param operationId 分配的操作标识id
     * @param args 要生成快照的方法对应的参数
     * */
    Snapshot<?> saveSnapshot(String operationId,Object[] args);

    /**
     * 生成日志信息
     * @param operationId 分配的操作标识id
     * @param args 要生成日志信息的方法对应的参数
     * */
    LogInfo<?> saveLog(String operationId,Object[] args);

    /**
     * 执行快照
     * @param operationId 操作标识id
     * */
    void executeSnapshot(String operationId);

    /**
     * 钩子方法，获取操作类型
     * */
    OperationType getOperationType();

    /**
     * 快照对象
     * */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    class Snapshot<T>{
        /*操作标识id*/
        private String id;
        /*操作类型（对应枚举：OperationType）*/
        private Integer type;
        /*快照内容*/
        private T data;
    }
    /**
     * 日志对象
     * */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    class LogInfo<T>{
        /*操作标识id*/
        private String id;
        /*操作发生时间*/
        private Timestamp time;
        /*操作类型（对应枚举：OperationType）*/
        private Integer type;
        /*概要*/
        private String outline;
        /*内容*/
        private String content;
        /*附件信息*/
        private T attachment;
    }
}
