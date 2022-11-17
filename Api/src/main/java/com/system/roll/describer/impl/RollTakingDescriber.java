package com.system.roll.describer.impl;

import com.system.roll.constant.impl.OperationType;
import com.system.roll.describer.Describer;

/**
 * 考勤操作的备份器
 * */
public class RollTakingDescriber implements Describer {
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
        return OperationType.TAKE_A_ROLL;
    }
}
