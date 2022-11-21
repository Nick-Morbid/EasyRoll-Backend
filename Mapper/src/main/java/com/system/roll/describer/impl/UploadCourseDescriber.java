package com.system.roll.describer.impl;

import com.system.roll.entity.constant.impl.OperationType;
import com.system.roll.describer.Describer;
import com.system.roll.redis.RollDataRedis;
import lombok.Data;

/**
 * 上传课程操作的备份器
 * */
@Data
public class UploadCourseDescriber implements Describer {
    private RollDataRedis rollDataRedis;
    @Override
    public Snapshot<?> saveSnapshot(String operationId,Object[] args) {
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
        return OperationType.UPLOAD_COURSE;
    }
}
