package com.system.roll.service.impl;

import com.system.roll.constant.impl.OperationType;
import com.system.roll.controller.SupervisorRollController;
import com.system.roll.describer.annotation.Operation;
import com.system.roll.entity.vo.message.MessageListVo;
import com.system.roll.entity.vo.roll.SingleRollStatisticVo;
import com.system.roll.entity.vo.student.StudentRollListVo;
import com.system.roll.service.SupervisorRollService;
import org.springframework.stereotype.Component;

@Component(value = "supervisorRollService")
public class SupervisorRollServiceImpl implements SupervisorRollService {
    @Override
    public void publishRoll(SupervisorRollController.RollDto data) {

    }

    @Override
    public StudentRollListVo getForm(String courseId) {
        return null;
    }

    @Override
    @Operation(type = OperationType.TAKE_A_ROLL)
    public SingleRollStatisticVo getRollDataStatistic(String courseId) {
        return null;
    }

    @Override
    public MessageListVo getMessageList() {
        return null;
    }

    @Override
    public void solveMessage(SupervisorRollController.SolveDto data) {

    }

    @Override
    public void outputExcel(String courseId) {

    }

    @Override
    public void outputText(Long courseId) {

    }
}
