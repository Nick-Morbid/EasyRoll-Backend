package com.system.roll.service.professor.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.entity.constant.impl.RollState;
import com.system.roll.entity.pojo.*;
import com.system.roll.entity.vo.roll.GradeRollStatisticVo;
import com.system.roll.entity.vo.roll.RollDataVo;
import com.system.roll.entity.vo.roll.TotalRollStatisticVo;
import com.system.roll.entity.vo.student.StudentRollDataListVo;
import com.system.roll.entity.vo.student.StudentRollRecord;
import com.system.roll.mapper.*;
import com.system.roll.redis.CourseRedis;
import com.system.roll.service.professor.ProfessorRollService;
import com.system.roll.utils.DateUtil;
import com.system.roll.utils.EnumUtil;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Resource
    private RollStatisticsMapper rollStatisticsMapper;

    @Resource
    private CourseRelationMapper courseRelationMapper;

    @Resource
    private CourseRedis courseRedis;

    @Resource
    private DeliveryMapper deliveryMapper;

    @Resource
    private CourseArrangementMapper courseArrangementMapper;

    @Override
    public RollDataVo getRollData(String courseId) {
        RollDataVo rollDataVo = new RollDataVo();

        LambdaQueryWrapper<RollStatistics> rsqw = new LambdaQueryWrapper<>();
        rsqw.eq(RollStatistics::getCourseId,courseId).orderByDesc(RollStatistics::getDate);

        // 查出该课程的考勤统计数据
        List<RollStatistics> rollStatisticsList = rollStatisticsMapper.selectList(rsqw);
        if(rollStatisticsList == null) return rollDataVo;
        RollStatistics rollStatistics = rollStatisticsList.get(0);

        List<RollDataVo.Record> absenceList = new ArrayList<>();
        List<RollDataVo.Record> leaveList = new ArrayList<>();
        List<RollDataVo.Record> lateList = new ArrayList<>();

        // 封装部分视图属性
        rollDataVo
                .setAbsenceNum(rollStatistics.getAbsenceNum())
                .setCourseId(rollStatistics.getCourseId())
                .setEnrollNum(rollStatistics.getEnrollNum())
                .setCourseName(courseRedis.getCourseName(rollStatistics.getCourseId()))
                .setLeaveNum(rollStatistics.getLeaveNum())
                .setLateNum(rollStatistics.getLateNum())
                .setCurrentNum(rollDataVo.getEnrollNum())
                .setAttendanceNum(rollStatistics.getAttendanceNum())
                .setDate(rollStatistics.getDate());

        LambdaQueryWrapper<AttendanceRecord> aqw = new LambdaQueryWrapper<>();
        aqw.eq(AttendanceRecord::getCourseId,rollStatistics.getCourseId())
                .eq(AttendanceRecord::getDate,rollStatistics.getDate())
                .eq(AttendanceRecord::getPeriod,rollStatistics.getPeriod());

        List<AttendanceRecord> records = attendanceRecordMapper.selectList(aqw);
        records.forEach(record->{
            RollDataVo.Record student = new RollDataVo.Record(record.getStudentId(),record.getStudentName());
            switch (record.getState()){
                case ABSENCE: absenceList.add(student);break;
                case LEAVE: leaveList.add(student);break;
                case LATE:lateList.add(student);break;
                default:break;
            }
        });
        rollDataVo.setAbsenceList(absenceList);
        rollDataVo.setLeaveList(leaveList);
        rollDataVo.setLateList(lateList);

        return rollDataVo;
    }

    @Override
    public StudentRollRecord getOneClassMember(String courseId, String studentId) {
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
            leftRecord.setPeriod(leaveRecord.getPeriod())
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
    public TotalRollStatisticVo getStatistic(String courseId, Integer sortRole) {
        TotalRollStatisticVo totalRollStatisticVo = new TotalRollStatisticVo();
        List<TotalRollStatisticVo.Record> records = new ArrayList<>();
        totalRollStatisticVo.setTotal(0);
        LambdaQueryWrapper<RollStatistics> rsqw = new LambdaQueryWrapper<>();
        rsqw.eq(RollStatistics::getCourseId,courseId);

        // 查出该课程考勤数据
        List<RollStatistics> rollStatistics = rollStatisticsMapper.selectList(rsqw);
        totalRollStatisticVo.setEnrollNum(rollStatistics.get(0).getEnrollNum());
        rollStatistics.forEach(roll->{
            TotalRollStatisticVo.Record record = new TotalRollStatisticVo.Record();
            record.setStatisticId(roll.getId())
                    .setWeekNo(dateUtil.getWeek(roll.getDate()))
                    .setWeekDay(dateUtil.getWeekDay(roll.getDate()))
                    .setPeriod(roll.getPeriod())
                    .setAttendanceNum(roll.getAttendanceNum())
                    .setAbsenceNum(roll.getAbsenceNum())
                    .setLeaveNum(roll.getLeaveNum())
                    .setLateNum(roll.getLateNum())
                    .setAttendanceRate((double)roll.getAttendanceNum()/roll.getEnrollNum());
            records.add(record);
        });
        sortByRole(sortRole,records);
        totalRollStatisticVo.setStatisticsRecords(records);
        totalRollStatisticVo.setTotal(records.size());
        return totalRollStatisticVo;
    }

    @Override
    public StudentRollDataListVo getClassMembers(String courseId, Integer sortRule) {
        StudentRollDataListVo studentRollDataListVo = new StudentRollDataListVo();
        studentRollDataListVo.setTotal(0);
        List<StudentRollDataListVo.StudentRollData> students = new ArrayList<>();

        LambdaQueryWrapper<RollStatistics> rsqw = new LambdaQueryWrapper<>();
        rsqw.eq(RollStatistics::getCourseId,courseId);
        // 查询该课程共上了几次
        int totalNum = Math.toIntExact(rollStatisticsMapper.selectCount(rsqw));
        if (totalNum == 0) return studentRollDataListVo;

        // 查询该课程全部学生
        LambdaQueryWrapper<CourseRelation> crqw = new LambdaQueryWrapper<>();
        crqw.eq(CourseRelation::getCourseId,courseId);
        List<CourseRelation> relations = courseRelationMapper.selectList(crqw);

        // 所处考勤情况并封装vo
        relations.forEach(relation->{
            StudentRollDataListVo.StudentRollData studentRollData = new StudentRollDataListVo.StudentRollData();
            String studentId = relation.getStudentId();
            studentRollData.setStudentId(studentId)
                            .setName(relation.getStudentName());
            Integer absenceNum = selectCount(RollState.ABSENCE, studentId);
            Integer leaveNum = selectCount(RollState.LEAVE, studentId);
            Integer lateNum = selectCount(RollState.LATE, studentId);
            Integer attendanceNum = totalNum - absenceNum - leaveNum;

            studentRollData.setAbsenceNum(absenceNum)
                    .setAttendanceNum(attendanceNum)
                    .setLeaveNum(leaveNum)
                    .setLateNum(lateNum);
            students.add(studentRollData);
        });
        List<StudentRollDataListVo.StudentRollData> sortedStudents = sortByRule(sortRule, students);

        // 进行排序
        studentRollDataListVo.setStudents(sortedStudents);
        studentRollDataListVo.setTotal(sortedStudents.size());

        return studentRollDataListVo;

    }

    @Override
    public GradeRollStatisticVo getAll(Date date) {
        GradeRollStatisticVo gradeRollStatisticVo = new GradeRollStatisticVo();
        gradeRollStatisticVo.setTotal(0);
        List<GradeRollStatisticVo.GradeRollData> records = new ArrayList<>();

        // 查出当天所有考勤数据
        LambdaQueryWrapper<RollStatistics> rsqw = new LambdaQueryWrapper<>();
        rsqw.eq(RollStatistics::getDate,date);
        List<RollStatistics> rollStatistics = rollStatisticsMapper.selectList(rsqw);
        rollStatistics.forEach(roll->{
            GradeRollStatisticVo.GradeRollData rollData = new GradeRollStatisticVo.GradeRollData();
            rollData.setCourseName(courseRedis.getCourseName(roll.getCourseId()))
                    .setAbsenceNum(roll.getAbsenceNum())
                    .setLateNum(roll.getLateNum())
                    .setLeaveNum(roll.getLeaveNum());

            // 查出课程教师
            LambdaQueryWrapper<Delivery> dqw = new LambdaQueryWrapper<>();
            dqw.eq(Delivery::getCourseId,roll.getCourseId());
            List<Delivery> deliveries = deliveryMapper.selectList(dqw);
            List<String> professorName = deliveries.stream().map(Delivery::getProfessorName).collect(Collectors.toList());
            String professorNames = StringUtils.join(professorName,',');
            rollData.setProfessorName(professorNames);

            // 查出课程教师
            LambdaQueryWrapper<Course> caqw = new LambdaQueryWrapper<>();
            caqw.eq(Course::getId,roll.getCourseId());
            Course course = courseMapper.selectOne(caqw);
            rollData.setClassroomNo(course.getClassroomNo());


            LambdaQueryWrapper<AttendanceRecord> aqw = new LambdaQueryWrapper<>();
            aqw.eq(AttendanceRecord::getCourseId,roll.getCourseId())
                    .eq(AttendanceRecord::getDate,date);

            List<AttendanceRecord> list = attendanceRecordMapper.selectList(aqw);
            List<String> absenceList = new ArrayList<>();
            List<String> lateList = new ArrayList<>();
            List<String> leaveList = new ArrayList<>();

            list.forEach(item->{
                switch (item.getState()){
                    case ABSENCE:absenceList.add(item.getStudentName());break;
                    case LEAVE:leaveList.add(item.getStudentName());break;
                    case LATE:lateList.add(item.getStudentName());break;
                    default:break;
                }
            });
            rollData.setAbsenceList(absenceList);
            rollData.setLateList(lateList);
            rollData.setLeaveList(leaveList);

            records.add(rollData);

        });
        gradeRollStatisticVo.setRollDataList(records);

        return gradeRollStatisticVo;
    }

    private void sortByRole(Integer sortRule,List<TotalRollStatisticVo.Record> students){
        switch (sortRule){
            case 1:students.sort(Comparator.comparing(TotalRollStatisticVo.Record::getAbsenceNum));break;
            case 2:students.sort(Comparator.comparing(TotalRollStatisticVo.Record::getAttendanceRate));break;
            default:break;
        }
    }


    private List<StudentRollDataListVo.StudentRollData> sortByRule(Integer sortRule,List<StudentRollDataListVo.StudentRollData> students){
        switch (sortRule){
            case 0:students.sort(Comparator.comparing(StudentRollDataListVo.StudentRollData::getStudentId));break;
            case 1:students.sort(Comparator.comparing(StudentRollDataListVo.StudentRollData::getAbsenceNum));break;
            case 2:students.sort(Comparator.comparing(StudentRollDataListVo.StudentRollData::getAttendanceNum));break;
            default:break;
        }

        return students;
    }

    private Integer selectCount(RollState rollState,String studentId){
        LambdaQueryWrapper<AttendanceRecord> aqw = new LambdaQueryWrapper<>();
        aqw.eq(AttendanceRecord::getStudentId,studentId)
                .eq(AttendanceRecord::getState,rollState);
        return Math.toIntExact(attendanceRecordMapper.selectCount(aqw));
    }
}
