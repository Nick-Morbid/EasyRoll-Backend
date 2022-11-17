package com.system.roll.service;

import com.system.roll.controller.SupervisorRollController;
import com.system.roll.entity.vo.message.MessageListVo;
import com.system.roll.entity.vo.roll.SingleRollStatisticVo;
import com.system.roll.entity.vo.student.StudentRollListVo;

public interface SupervisorRollService {

    /**
     * 发布定位签到
     * */
    void publishRoll(SupervisorRollController.RollDto data);

    /**
     * 获取点名表单
     * */
    StudentRollListVo getForm(String courseId);

    /**
     * 获取单次点名的统计结果
     * */
    SingleRollStatisticVo getRollDataStatistic(String courseId) throws InterruptedException;

    /**
     * 获取消息列表
     * */
    MessageListVo getMessageList();

    /**
     * 处理申诉消息
     * */
    void solveMessage(SupervisorRollController.SolveDto data);

    /**
     * 导出为excel
     * */
    void outputExcel(String courseId);

    /**
     * 导出为text
     * */
    void outputText(Long courseId);
}
