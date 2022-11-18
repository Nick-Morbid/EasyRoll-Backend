package com.system.roll.service.impl;

import com.system.roll.constant.impl.OperationType;
import com.system.roll.constant.impl.ResultCode;
import com.system.roll.constant.impl.RollState;
import com.system.roll.controller.SupervisorRollController;
import com.system.roll.describer.annotation.Operation;
import com.system.roll.entity.pojo.RollData;
import com.system.roll.entity.vo.message.MessageListVo;
import com.system.roll.entity.vo.roll.SingleRollStatisticVo;
import com.system.roll.entity.vo.student.StudentRollListVo;
import com.system.roll.exception.impl.ServiceException;
import com.system.roll.redis.RollDataRedis;
import com.system.roll.service.SupervisorRollService;
import com.system.roll.utils.EnumUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component(value = "SupervisorRollService")
public class SupervisorRollServiceImpl implements SupervisorRollService {
    @Resource(name = "RollDataRedis")
    private RollDataRedis rollDataRedis;
    @Resource
    private EnumUtil enumUtil;
    @Override
    public void publishRoll(SupervisorRollController.RollDto data) {

    }

    @Override
    public StudentRollListVo getForm(String courseId) {
        return null;
    }

    @Override
    @Operation(type = OperationType.TAKE_A_ROLL)
    public SingleRollStatisticVo getRollDataStatistic(String courseId) throws InterruptedException {
        /*获取redis中的记录*/
        int count = 1000;
        while (!(count-- <= 0)){
            if (rollDataRedis.listIsExist(courseId)) break;
            Thread.sleep(100);
        }
        if (!rollDataRedis.listIsExist(courseId)) throw new ServiceException(ResultCode.SERVER_ERROR);
        SingleRollStatisticVo singleRollStatisticVo = new SingleRollStatisticVo();
        List<RollData> rollDataList = rollDataRedis.getRollDataList(courseId);
        rollDataList.forEach(rollData -> {
            RollState rollState = enumUtil.getEnumByCode(RollState.class, rollData.getState());
            switch (rollState){
                case ATTENDANCE:
                    singleRollStatisticVo.setAttendanceNum(singleRollStatisticVo.getAttendanceNum()+1);break;
                case LEAVE:
                    singleRollStatisticVo.setLeaveNum(singleRollStatisticVo.getLeaveNum()+1);break;
                case LATE:
                    singleRollStatisticVo.setLateNum(singleRollStatisticVo.getLateNum()+1);
                    singleRollStatisticVo.setAttendanceNum(singleRollStatisticVo.getAttendanceNum()+1);break;
                case ABSENCE:
                    singleRollStatisticVo.setAbsenceNum(singleRollStatisticVo.getAbsenceNum()+1);break;
            }
        });
        singleRollStatisticVo.setEnrollNum(rollDataList.size());
        return singleRollStatisticVo;
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
