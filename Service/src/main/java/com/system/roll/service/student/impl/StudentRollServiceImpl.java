package com.system.roll.service.student.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.entity.pojo.Leave;
import com.system.roll.entity.vo.leave.LeaveListVo;
import com.system.roll.entity.vo.leave.LeaveVo;
import com.system.roll.entity.vo.student.RollHistoryVo;
import com.system.roll.mapper.LeaveMapper;
import com.system.roll.handler.mapstruct.LeaveConvertor;
import com.system.roll.context.security.SecurityContextHolder;
import com.system.roll.service.student.StudentRollService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Date;
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
    public void applyQuery(LeaveQueryDTO leaveDto) {
        // TODO
    }

    @Override
    public RollHistoryVo getHistory(Date date) {
        

        return null;
    }
}
