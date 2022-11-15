package com.system.roll.flink.task;

/**
 * flink计算任务的定义模板
 * */
public interface FlinkTask {
    /**
     * 定义任务
     * */
    void defineTask();
    /**
     * 执行任务
     * */
    void execute() throws Exception;
}
