package com.system.roll.exporter.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.entity.constant.impl.Period;
import com.system.roll.entity.constant.impl.RollState;
import com.system.roll.entity.pojo.AttendanceRecord;
import com.system.roll.entity.pojo.Course;
import com.system.roll.entity.pojo.Delivery;
import com.system.roll.exporter.TextExporter;
import com.system.roll.mapper.AttendanceRecordMapper;
import com.system.roll.mapper.CourseMapper;
import com.system.roll.mapper.DeliveryMapper;
import com.system.roll.utils.CommonUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.List;

@Component(value = "TextExporter")
public class TextExporterImpl implements TextExporter {

    @Resource
    private CourseMapper courseMapper;
    @Resource
    private DeliveryMapper deliveryMapper;
    @Resource
    private CommonUtil commonUtil;
    @Resource
    private AttendanceRecordMapper attendanceRecordMapper;

    @Override
    public String outputText(String courseId, Date date, Period period) {
        Course course = courseMapper.selectById(courseId);
        String courseName = course.getCourseName();
        LambdaQueryWrapper<Delivery> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Delivery::getCourseId,courseId);
        List<Delivery> deliveries = deliveryMapper.selectList(wrapper1);
        StringBuilder professorNames = new StringBuilder();
        for (int i = 0; i < deliveries.size(); i++) {
            if (i!=0) professorNames.append(",");
            Delivery delivery = deliveries.get(0);
            String professorName = delivery.getProfessorName();
            professorNames.append(professorName);
        }
        String classroom = commonUtil.getClassroom(course.getClassroomNo());
        LambdaQueryWrapper<AttendanceRecord> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(AttendanceRecord::getCourseId,courseId)
                .eq(AttendanceRecord::getDate,date)
                .eq(AttendanceRecord::getPeriod,period)
                .eq(AttendanceRecord::getState, RollState.LATE);
        List<AttendanceRecord> lateRecords = attendanceRecordMapper.selectList(wrapper2);
        wrapper2.clear();
        wrapper2.eq(AttendanceRecord::getCourseId,courseId)
                .eq(AttendanceRecord::getDate,date)
                .eq(AttendanceRecord::getPeriod,period)
                .eq(AttendanceRecord::getState, RollState.ABSENCE);
        List<AttendanceRecord> absenceRecords = attendanceRecordMapper.selectList(wrapper2);
        wrapper2.clear();
        wrapper2.eq(AttendanceRecord::getCourseId,courseId)
                .eq(AttendanceRecord::getDate,date)
                .eq(AttendanceRecord::getPeriod,period)
                .eq(AttendanceRecord::getState, RollState.LEAVE);
        List<AttendanceRecord> leaveRecords = attendanceRecordMapper.selectList(wrapper2);
        StringBuilder lateStr = new StringBuilder();
        StringBuilder absenceStr = new StringBuilder();
        StringBuilder leaveStr = new StringBuilder();
        if (lateRecords==null||lateRecords.size()==0){
            lateStr.append("无");
        }else {
            for (int i = 0; i < lateRecords.size(); i++) {
                if (i!=0) lateStr.append(",");
                lateStr.append(lateRecords.get(i).getStudentName());
            }
        }

        if (absenceRecords==null||absenceRecords.size()==0){
            absenceStr.append("无");
        }else {
            for (int i = 0; i < absenceRecords.size(); i++) {
                if (i!=0) absenceStr.append(",");
                absenceStr.append(absenceRecords.get(i).getStudentName());
            }
        }

        if (leaveRecords==null||leaveRecords.size()==0){
            leaveStr.append("无");
        }else {
            for (int i = 0; i < leaveRecords.size(); i++) {
                if (i!=0) leaveStr.append(",");
                leaveStr.append(leaveRecords.get(i).getStudentName());
            }
        }

        return String.format("课程名称：%s\n" +
                "任课老师：%s\n" +
                "上课教室：%s\n" +
                "迟到：%s\n" +
                "缺课：%s\n" +
                "请假：%s", courseName, professorNames, classroom, lateStr, absenceStr, leaveStr);
    }
}
