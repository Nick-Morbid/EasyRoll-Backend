package com.system.roll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.controller.StudentRollController;
import com.system.roll.entity.pojo.Leave;
import com.system.roll.entity.vo.leave.LeaveListVo;
import com.system.roll.entity.vo.leave.LeaveVo;
import com.system.roll.mapper.LeaveMapper;
import com.system.roll.mapstruct.LeaveConvertor;
import com.system.roll.security.context.SecurityContextHolder;
import com.system.roll.service.StudentRollService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component(value = "studentRollService")
public class StudentRollServiceImpl implements StudentRollService {
    @Resource
    private LeaveMapper leaveMapper;

    @Override
    public LeaveListVo getAllLeave() {

        String studentId = SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "id");
        LambdaQueryWrapper<Leave> leaveQueryWrapper = new LambdaQueryWrapper<>();

        // 获取该学生的所有请假记录
        leaveQueryWrapper.eq(Leave::getStudentId,studentId);
        List<Leave> leaves = leaveMapper.selectList(leaveQueryWrapper);
        LeaveListVo leaveRecords = new LeaveListVo();
        leaveRecords.setTotal(leaves.size());

        List<LeaveVo> leaveVos = new ArrayList<>();
        leaves.forEach(leave-> leaveVos.add(LeaveConvertor.INSTANCE.LeaveToLeaveVo(leave)));
        leaveRecords.setLeaveRecords(leaveVos);

        return leaveRecords;
    }

    @Override
    public LeaveVo leaveQuery(String leaveId) {
        Leave leave = leaveMapper.selectById(leaveId);
        return LeaveConvertor.INSTANCE.LeaveToLeaveVo(leave);
    }

    @Override
    public void applyQuery(StudentRollController.LeaveQueryDTO leaveDto) {
        // TODO
    }
}
