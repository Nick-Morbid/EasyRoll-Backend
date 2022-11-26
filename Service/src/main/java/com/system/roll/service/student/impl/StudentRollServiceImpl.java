package com.system.roll.service.student.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.entity.pojo.LeaveRelation;
import com.system.roll.entity.vo.leave.LeaveListVo;
import com.system.roll.entity.vo.leave.LeaveVo;
import com.system.roll.entity.vo.student.RollHistoryVo;
import com.system.roll.handler.mapstruct.LeaveConvertor;
import com.system.roll.context.security.SecurityContextHolder;
import com.system.roll.mapper.LeaveRelationMapper;
import com.system.roll.service.student.StudentRollService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component(value = "studentRollService")
public class StudentRollServiceImpl implements StudentRollService {
    @Resource
    private LeaveRelationMapper leaveRelationMapper;

    @Override
    public LeaveListVo getAllLeave() {

        String studentId = SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "id");
        LambdaQueryWrapper<LeaveRelation> leaveQueryWrapper = new LambdaQueryWrapper<>();

        // 获取该学生的所有请假记录
        leaveQueryWrapper.eq(LeaveRelation::getStudentId,studentId);
        List<LeaveRelation> leaves = leaveRelationMapper.selectList(leaveQueryWrapper);
        LeaveListVo leaveRecords = new LeaveListVo();
        leaveRecords.setTotal(leaves.size());

        List<LeaveVo> leaveVos = new ArrayList<>();
        leaves.forEach(leave-> leaveVos.add(LeaveConvertor.INSTANCE.LeaveToLeaveVo(leave)));
        leaveRecords.setLeaveRecords(leaveVos);

        return leaveRecords;
    }

    @Override
    public LeaveVo leaveQuery(String leaveId) {
        LeaveRelation leave = leaveRelationMapper.selectById(leaveId);
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
