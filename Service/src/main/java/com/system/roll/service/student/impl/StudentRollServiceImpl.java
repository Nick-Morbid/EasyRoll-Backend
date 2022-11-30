package com.system.roll.service.student.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.entity.constant.impl.RollState;
import com.system.roll.entity.pojo.*;
import com.system.roll.entity.vo.leave.LeaveListVo;
import com.system.roll.entity.vo.leave.LeaveVo;
import com.system.roll.entity.vo.student.RollHistoryVo;
import com.system.roll.mapper.*;
import com.system.roll.handler.mapstruct.LeaveConvertor;
import com.system.roll.context.security.SecurityContextHolder;
import com.system.roll.redis.CourseRedis;
import com.system.roll.redis.StudentRedis;
import com.system.roll.service.student.StudentRollService;
import com.system.roll.utils.DateUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component(value = "studentRollService")
public class StudentRollServiceImpl implements StudentRollService {
    @Resource
    private LeaveRelationMapper leaveMapper;

    @Resource
    private CourseRelationMapper courseRelationMapper;

    @Resource
    private AttendanceRecordMapper attendanceRecordMapper;

    @Resource
    private RollRelationMapper rollRelationMapper;

    @Resource
    private CourseRedis courseRedis;

    @Resource
    private StudentRedis studentRedis;

    @Resource
    private CourseArrangementMapper courseArrangementMapper;

    @Resource
    private DateUtil dateUtil;

    @Override
    public LeaveListVo getAllLeave() {

        String studentId = SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "id");
        LambdaQueryWrapper<LeaveRelation> leaveQueryWrapper = new LambdaQueryWrapper<>();

        // 获取该学生的所有请假记录
        leaveQueryWrapper.eq(LeaveRelation::getStudentId,studentId);
        List<LeaveRelation> leaves = leaveMapper.selectList(leaveQueryWrapper);
        LeaveListVo leaveRecords = new LeaveListVo();
        leaveRecords.setTotal(leaves.size());

        List<LeaveVo> leaveVos = new ArrayList<>();
        leaves.forEach(leave-> leaveVos.add(LeaveConvertor.INSTANCE.LeaveToLeaveVo(leave)));
        leaveRecords.setLeaveRecords(leaveVos);

        return leaveRecords;
    }

    @Override
    public LeaveVo leaveQuery(String leaveId) {
        LeaveRelation leave = leaveMapper.selectById(leaveId);
        return LeaveConvertor.INSTANCE.LeaveToLeaveVo(leave);
    }

    @Override
    public void applyQuery(LeaveQueryDTO leaveDto) {
        // TODO
    }

    @Override
    public RollHistoryVo getHistory(Date date) {

        // 获取学生id
        String studentId = SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "id");
        RollHistoryVo rollHistoryVo = new RollHistoryVo();
        List<RollHistoryVo.RollRecord> rollRecords = new ArrayList<>();

        // 查出该学生选的所有课程
        LambdaQueryWrapper<CourseRelation> crqw = new LambdaQueryWrapper<>();
        crqw.eq(CourseRelation::getStudentId,studentId);
        List<CourseRelation> courseRelations = courseRelationMapper.selectList(crqw);

        // 查出学生指定日期的考勤记录
        LambdaQueryWrapper<AttendanceRecord> aqw = new LambdaQueryWrapper<>();
        courseRelations.forEach(relation -> {
            RollHistoryVo.RollRecord rollRecord = new RollHistoryVo.RollRecord();
            // 查出period
            String courseId = relation.getCourseId();
            LambdaQueryWrapper<CourseArrangement> caqw = new LambdaQueryWrapper<>();
            caqw.eq(CourseArrangement::getCourseId,courseId)
                    .eq(CourseArrangement::getWeekDay,dateUtil.getWeekDay(date));
            CourseArrangement courseArrangement = courseArrangementMapper.selectOne(caqw);
            rollRecord.setPeriod(courseArrangement.getPeriod());

            // 查出课程名称
            String courseName = courseRedis.getCourseName(courseId);
            rollRecord.setCourseId(courseId)
                    .setCourseName(courseName);

            aqw.clear();
            aqw.eq(AttendanceRecord::getStudentId,studentId)
                    .eq(AttendanceRecord::getCourseId, courseId)
                    .eq(AttendanceRecord::getDate,date);
            AttendanceRecord attendanceRecord = attendanceRecordMapper.selectOne(aqw);

            // 查出负责该课程的督导员
            LambdaQueryWrapper<RollRelation> rlqw = new LambdaQueryWrapper<>();
            rlqw.eq(RollRelation::getCourseId, courseId);
            RollRelation supervisor = rollRelationMapper.selectOne(rlqw);
            String supervisorName = studentRedis.getName(supervisor.getSupervisorId());
            rollRecord.setSupervisorId(supervisor.getSupervisorId())
                    .setSupervisorName(supervisorName);

            // 没查出来，说明当天该门课程该学生正常出勤
            if(attendanceRecord == null){
                rollRecord.setState(RollState.ATTENDANCE);
            }else{
                rollRecord.setState(attendanceRecord.getState());
            }

            rollRecords.add(rollRecord);
        });
        rollHistoryVo.setRollRecords(rollRecords);

        return rollHistoryVo;
    }

    @Override
    public Boolean putPosition(RollTaskDTO rollTaskDTO) {

        return true;
    }
}
