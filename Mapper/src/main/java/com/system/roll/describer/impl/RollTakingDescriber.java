package com.system.roll.describer.impl;

import com.system.roll.entity.constant.impl.OperationType;
import com.system.roll.describer.Describer;
import com.system.roll.redis.RollDataRedis;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * 考勤操作的备份器
 * */
@Data
public class RollTakingDescriber implements Describer {
    private RollDataRedis rollDataRedis;

    @Override
    public Snapshot<?> saveSnapshot(String operationId,Object[] args) {
        /*读取参数*/
        String courseId = String.valueOf(args[0]);
        return null;
    }

    @Override
    public LogInfo<?> saveLog(String operationId,Object[] args) {
        return null;
    }

    @Override
    public void executeSnapshot(String operationId) {

    }

    @Override
    public OperationType getOperationType() {
        return OperationType.TAKE_A_ROLL;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class SnapshotData{
        private Timestamp time;
        private Integer period;
        private String courseId;
    }
}
