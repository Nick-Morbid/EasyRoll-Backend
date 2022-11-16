package com.system.roll.service;

import com.system.roll.controller.StudentRollController;
import com.system.roll.entity.vo.leave.LeaveListVo;
import com.system.roll.entity.vo.leave.LeaveVo;

public interface StudentRollService {
    LeaveListVo getAllLeave();

    LeaveVo leaveQuery(String leaveId);

    void applyQuery(StudentRollController.LeaveQueryDTO leaveDto);
}
