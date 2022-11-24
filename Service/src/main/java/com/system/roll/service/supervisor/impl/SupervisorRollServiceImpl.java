package com.system.roll.service.supervisor.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.context.security.SecurityContextHolder;
import com.system.roll.entity.constant.impl.OperationType;
import com.system.roll.entity.constant.impl.Period;
import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.constant.impl.RollState;
import com.system.roll.describer.annotation.Operation;
import com.system.roll.entity.pojo.*;
import com.system.roll.entity.vo.message.MessageListVo;
import com.system.roll.entity.vo.roll.SingleRollStatisticVo;
import com.system.roll.entity.vo.roll.statistics.StatisticDetailVo;
import com.system.roll.entity.vo.roll.statistics.StatisticsVo;
import com.system.roll.entity.vo.student.StudentRollListVo;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.mapper.*;
import com.system.roll.handler.mapstruct.StudentConvertor;
import com.system.roll.redis.CourseRedis;
import com.system.roll.redis.RollDataRedis;
import com.system.roll.redis.StudentRedis;
import com.system.roll.service.supervisor.SupervisorRollService;
import com.system.roll.utils.DateUtil;
import com.system.roll.utils.EnumUtil;
import com.system.roll.utils.PinyinUtil;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component(value = "SupervisorRollService")
public class SupervisorRollServiceImpl implements SupervisorRollService {
    @Resource(name = "RollDataRedis")
    private RollDataRedis rollDataRedis;
    @Resource
    private EnumUtil enumUtil;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private StudentConvertor studentConvertor;
    @Resource
    private StudentRedis studentRedis;
    @Resource
    private PinyinUtil pinyinUtil;

    @Resource
    private RollStatisticsMapper rollStatisticsMapper;

    @Resource
    private RollRelationMapper rollRelationMapper;

    @Resource
    private DateUtil dateUtil;

    @Resource
    private CourseArrangementMapper courseArrangementMapper;

    @Resource
    private CourseRedis courseRedis;

    @Resource
    private AttendanceRecordMapper attendanceRecordMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private DeliveryMapper deliveryMapper;

    @Override
    public void publishRoll(RollDto data) {

    }

    @Override
    public StudentRollListVo getForm(String courseId) {
        StudentRollListVo studentRollListVo = new StudentRollListVo();
        /*先暂定为获取所有的学生*/
        List<Student> studentList = studentMapper.selectListByCourseId(courseId);
        /*进行数据组装*/
        List<StudentRollListVo.StudentRollVo> studentList1 = studentList.stream().map(studentConvertor::studentToStudentRollVo).peek(studentRollVo -> {
            /*获取拼音*/
            String[] pinYin = studentRedis.getPinYin(studentRollVo.getId());
            if (pinYin.length == 0) {
                pinYin = pinyinUtil.toPinyin(studentRollVo.getName());
                studentRedis.savePinYin(studentRollVo.getId(), pinYin);
            }
            studentRollVo.setPinyin(Arrays.asList(pinYin));
        }).collect(Collectors.toList());
        studentRollListVo.setStudents(studentList1);
        studentRollListVo.setTotal(studentList1.size());
        return studentRollListVo;
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
    public void solveMessage(SolveDto data) {

    }

    @Override
    public void outputExcel(String courseId) {

    }

    @Override
    public void outputText(Long courseId) {

    }

    @Override
    public StatisticsVo getStatistics(Date date) {
        String supervisorId = SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "id");
        StatisticsVo statisticsVo = new StatisticsVo();
        List<StatisticsVo.Record> records = new ArrayList<>();
        statisticsVo.setTotal(0);
        // 1.获取当前督导员所点的所有课程
        LambdaQueryWrapper<RollRelation> rrqw = new LambdaQueryWrapper<>();
        rrqw.eq(RollRelation::getSupervisorId,supervisorId);
        List<RollRelation> rollRelations = rollRelationMapper.selectList(rrqw);
        if(rollRelations == null || rollRelations.size() == 0){
            return statisticsVo;
        }

        List<String> courseIds = rollRelations.stream().map(RollRelation::getCourseId).collect(Collectors.toList());

        // 2.获取该督导员指定日期的点名数据
        LambdaQueryWrapper<RollStatistics> rsqw = new LambdaQueryWrapper<>();
        rsqw.eq(RollStatistics::getDate,dateUtil.getWeekDay(date))
                .in(RollStatistics::getCourseId,courseIds);
        List<RollStatistics> rollStatistics = rollStatisticsMapper.selectList(rsqw);
        LambdaQueryWrapper<CourseArrangement> caqw = new LambdaQueryWrapper<>();
        rollStatistics.forEach(statistic->{
            // 查询课程相关信息
            caqw.clear();
            caqw.eq(CourseArrangement::getCourseId,statistic.getCourseId());

            List<CourseArrangement> courses = courseArrangementMapper.selectList(caqw);
            courses.forEach(course->{
                StatisticsVo.Record record = new StatisticsVo.Record();
                record.setStatisticId(statistic.getId())
                        .setCourseId(statistic.getCourseId())
                        .setCourseName(courseRedis.getCourseName(statistic.getCourseId()))
                        .setWeekNo(dateUtil.getWeek(date))
                        .setWeekDay(course.getWeekDay())
                        .setPeriod(course.getPeriod())
                        .setDate(date)
                        .setEnrollNum(statistic.getEnrollNum())
                        .setAttendanceNum(statistic.getAttendanceNum())
                        .setLeaveNum(statistic.getLeaveNum())
                        .setLateNum(statistic.getLateNum())
                        .setAttendanceRate((double) (statistic.getAttendanceNum() / statistic.getEnrollNum()));
                records.add(record);
            });
        });

        statisticsVo.setStatisticsRecords(records);
        statisticsVo.setTotal(records.size());

        return statisticsVo;
    }

    @Override
    public StatisticDetailVo getStatisticDetail(String statisticsId) {
        StatisticDetailVo statisticDetailVo = new StatisticDetailVo();
        List<StatisticDetailVo.Record> absenceList = new ArrayList<>();
        List<StatisticDetailVo.Record> leaveList = new ArrayList<>();
        List<StatisticDetailVo.Record> lateList = new ArrayList<>();

        // 查出考勤数据
        RollStatistics rollStatistics = rollStatisticsMapper.selectById(statisticsId);

        // 封装视图对象
        statisticDetailVo.setAttendanceNum(rollStatistics.getAttendanceNum())
                .setLeaveNum(rollStatistics.getLeaveNum())
                .setLateNum(rollStatistics.getLateNum())
                .setAttendanceRate((double) (rollStatistics.getAttendanceNum() / rollStatistics.getEnrollNum()));

        // 查出课程数据
        Course course = courseMapper.selectById(rollStatistics.getCourseId());

        // 封装视图对象
        statisticDetailVo.setCourseId(course.getId())
                .setStartWeek(course.getStartWeek())
                .setEndWeek(course.getEndWeek())
                .setName(course.getCourseName())
                .setEnrollNum(course.getEnrollNum());

        // 查出课程教师
        LambdaQueryWrapper<Delivery> dqw = new LambdaQueryWrapper<>();
        dqw.eq(Delivery::getCourseId,course.getId());
        List<Delivery> deliveries = deliveryMapper.selectList(dqw);
        List<String> names = deliveries.stream().map(Delivery::getProfessorName).collect(Collectors.toList());
        String concatName = StringUtils.join(names);

        // 封装视图对象
        statisticDetailVo.setProfessorName(concatName);

        // 查出出勤记录
        LambdaQueryWrapper<AttendanceRecord> arlw = new LambdaQueryWrapper<>();
        arlw.eq(AttendanceRecord::getCourseId,rollStatistics.getCourseId())
                .eq(AttendanceRecord::getDate,rollStatistics.getDate());
        List<AttendanceRecord> records = attendanceRecordMapper.selectList(arlw);

        records.forEach(record->{
            statisticDetailVo.setPeriod(record.getPeriod());
            StatisticDetailVo.Record student = new StatisticDetailVo.Record(record.getId(), record.getStudentName());
            switch (record.getState()){
                case ABSENCE: absenceList.add(student);break;
                case LEAVE: leaveList.add(student);break;
                case LATE: lateList.add(student);break;
                default:break;
            }
        });
        statisticDetailVo.setAbsenceList(absenceList);
        statisticDetailVo.setLeaveList(leaveList);
        statisticDetailVo.setLateList(lateList);

        return statisticDetailVo;
    }
}
