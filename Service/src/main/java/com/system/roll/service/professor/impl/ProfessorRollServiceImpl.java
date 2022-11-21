package com.system.roll.service.professor.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.entity.constant.impl.RollState;
import com.system.roll.entity.constant.impl.SortRole;
import com.system.roll.entity.pojo.AttendanceRecord;
import com.system.roll.entity.pojo.Course;
import com.system.roll.entity.vo.roll.RollDataVo;
import com.system.roll.entity.vo.roll.TotalRollStatisticVo;
import com.system.roll.entity.vo.student.StudentRollRecord;
import com.system.roll.mapper.AttendanceRecordMapper;
import com.system.roll.mapper.CourseMapper;
import com.system.roll.service.professor.ProfessorRollService;
import com.system.roll.utils.DateUtil;
import com.system.roll.utils.EnumUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component(value = "ProfessorRollService")
public class ProfessorRollServiceImpl implements ProfessorRollService {
    @Resource
    private AttendanceRecordMapper attendanceRecordMapper;

    @Resource
    private DateUtil dateUtil;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private EnumUtil enumUtil;

    @Override
    public RollDataVo getRollData(Long courseId) {
        LambdaQueryWrapper<AttendanceRecord> aqw = new LambdaQueryWrapper<>();

        return null;
    }

    @Override
    public StudentRollRecord getClassMembers(Long courseId, Long studentId) {
        LambdaQueryWrapper<AttendanceRecord> aqw = new LambdaQueryWrapper<>();

        aqw.eq(AttendanceRecord::getCourseId,courseId)
                .eq(AttendanceRecord::getStudentId,studentId)
                .eq(AttendanceRecord::getState, RollState.ABSENCE);
        List<AttendanceRecord> absenceRecords = attendanceRecordMapper.selectList(aqw);

        StudentRollRecord studentRollRecord = new StudentRollRecord();
        studentRollRecord.setAbsenceRecords(new ArrayList<>());
        absenceRecords.forEach(absenceRecord->{
            StudentRollRecord.Record absentRecord = new StudentRollRecord.Record();
            absentRecord.setPeriod(absenceRecord.getPeriod())
                    .setWeekDay(dateUtil.getWeekDay(absenceRecord.getDate()))
                    .setWeekNo(dateUtil.getWeek(absenceRecord.getDate()));

            studentRollRecord.getAbsenceRecords().add(absentRecord);
        });

        aqw.clear();
        aqw.eq(AttendanceRecord::getCourseId,courseId)
                .eq(AttendanceRecord::getStudentId,studentId)
                .eq(AttendanceRecord::getState, RollState.LEAVE);
        List<AttendanceRecord> leaveRecords = attendanceRecordMapper.selectList(aqw);
        studentRollRecord.setLeaveRecords(new ArrayList<>());
        leaveRecords.forEach(leaveRecord->{
            StudentRollRecord.Record leftRecord = new StudentRollRecord.Record();
            leftRecord.setPeriod(leftRecord.getPeriod())
                    .setWeekDay(dateUtil.getWeekDay(leaveRecord.getDate()))
                    .setWeekNo(dateUtil.getWeek(leaveRecord.getDate()));
            studentRollRecord.getLeaveRecords().add(leftRecord);
        });

        aqw.clear();
        aqw.eq(AttendanceRecord::getCourseId,courseId)
                .eq(AttendanceRecord::getStudentId,studentId)
                .eq(AttendanceRecord::getState, RollState.LATE);
        List<AttendanceRecord> lateRecords = attendanceRecordMapper.selectList(aqw);
        studentRollRecord.setLateRecords(new ArrayList<>());
        lateRecords.forEach(late->{
            StudentRollRecord.Record lateRecord = new StudentRollRecord.Record();
            lateRecord.setPeriod(late.getPeriod())
                    .setWeekDay(dateUtil.getWeekDay(late.getDate()))
                    .setWeekNo(dateUtil.getWeek(late.getDate()));
            studentRollRecord.getLateRecords().add(lateRecord);
        });


        return studentRollRecord;
    }

    @Override
    public TotalRollStatisticVo getStatistic(Long courseId, Integer sortRole) {
        TotalRollStatisticVo totalRollStatisticVo = new TotalRollStatisticVo();
        Course course = courseMapper.selectById(courseId);
        Integer enrollNum = course.getEnrollNum();
        totalRollStatisticVo.setEnrollNum(enrollNum);


        return null;
    }
}
