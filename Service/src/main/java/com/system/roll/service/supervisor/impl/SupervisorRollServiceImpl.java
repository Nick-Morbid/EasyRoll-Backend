package com.system.roll.service.supervisor.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.context.security.SecurityContextHolder;
import com.system.roll.describer.annotation.Operation;
import com.system.roll.entity.constant.impl.*;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.entity.pojo.*;
import com.system.roll.entity.properites.CommonProperties;
import com.system.roll.entity.vo.message.MessageListVo;
import com.system.roll.entity.vo.roll.SingleRollStatisticVo;
import com.system.roll.entity.vo.roll.statistics.StatisticDetailVo;
import com.system.roll.entity.vo.roll.statistics.StatisticsVo;
import com.system.roll.entity.vo.student.StudentRollListVo;
import com.system.roll.formBuilder.FormBuilder;
import com.system.roll.handler.mapstruct.RollDataConvertor;
import com.system.roll.handler.mapstruct.StudentConvertor;
import com.system.roll.mapper.*;
import com.system.roll.redis.CourseRedis;
import com.system.roll.redis.RollDataRedis;
import com.system.roll.redis.StudentRedis;
import com.system.roll.service.supervisor.SupervisorRollService;
import com.system.roll.utils.DateUtil;
import com.system.roll.utils.EnumUtil;
import com.system.roll.utils.IdUtil;
import com.system.roll.utils.PinyinUtil;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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

    @Resource(name = "FormBuilder")
    private FormBuilder formBuilder;

    @Resource
    private RollDataConvertor rollDataConvertor;

    @Resource
    private IdUtil idUtil;

    @Resource
    private CommonProperties commonProperties;

    @Resource
    private LeaveRelationMapper leaveRelationMapper;

    @Override
    public void publishRoll(RollDto data) {

    }

    @Override
    public StudentRollListVo getForm(String courseId) {
        return formBuilder.getForm(courseId);
    }

    @Override
    @Transactional
    @Operation(type = OperationType.TAKE_A_ROLL)
    public SingleRollStatisticVo getRollDataStatistic(Integer enrollNum,String courseId) throws InterruptedException {
        String id = SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "id");
        String name = SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "name");
        /*获取redis中的记录*/
        SingleRollStatisticVo statistics = new SingleRollStatisticVo().setEnrollNum(enrollNum);
        int count = 5000;
        for (int i = 0; i < count; i++) {
            if (rollDataRedis.listIsExist(courseId)) break;
            Thread.sleep(100);
        }
        if (!rollDataRedis.listIsExist(courseId)) throw new ServiceException(ResultCode.RESOURCE_NOT_FOUND);
        /*生成课程时段信息*/
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Period period = commonProperties.getPeriod(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));
        if (period.equals(Period.NOT_IN_CLASS_TIME)){
            throw new ServiceException(ResultCode.INVALID_ROLL);
        }
        /*计算统计结果*/
        List<RollData> rollDataList = rollDataRedis.getRollDataList(courseId);
        for (RollData rollData : rollDataList) {
            RollState rollState = enumUtil.getEnumByCode(RollState.class, rollData.getState());
            switch (rollState) {
                case ATTENDANCE:
                    statistics.incrAttendanceNum();
                    break;
                case ABSENCE:
                    statistics.incrAbsenceNum();
                    break;
                case LATE:
                    statistics.incrLateNum();
                    statistics.incrAttendanceNum();
                    break;
                case LEAVE:
                    /*生成请假记录*/
                    LeaveRelation leaveRelation = new LeaveRelation()
                            .setId(idUtil.getId())
                            .setTransactorId(id)
                            .setTransactorName(name)
                            .setCreated(new Timestamp(System.currentTimeMillis()))
                            .setStudentId(rollData.getStudentId())
                            .setStudentName(studentRedis.getName(rollData.getStudentId()))
                            .setExcuse("督导队员标记为请假")
                            .setStartTime(new Date(System.currentTimeMillis()))
                            .setEndTime(new Date(System.currentTimeMillis()+commonProperties.ClassTime(TimeUnit.MINUTE)))
                            .setResult(1);
                    leaveRelationMapper.insert(leaveRelation);
                    /*todo 通知对应的学生*/
                    statistics.incrLeaveNum();
                    break;
            }
            /*非正常出勤，则生成相应的记录*/
            if (!rollState.equals(RollState.ATTENDANCE)){
                AttendanceRecord attendanceRecord = new AttendanceRecord()
                        .setId(idUtil.getId())
                        .setCourseId(courseId)
                        .setCreated(new Timestamp(System.currentTimeMillis()))
                        .setPeriod(period)
                        .setState(rollState)
                        .setStudentId(rollData.getStudentId())
                        .setStudentName(studentRedis.getName(rollData.getStudentId()))
                        .setSupervisorId(id)
                        .setDate(new Date(System.currentTimeMillis()));
                attendanceRecordMapper.insert(attendanceRecord);
            }
        }
        /*保存统计结果到数据库中*/
        RollStatistics rollStatistics = rollDataConvertor
                .SingleRollStatisticVoToRollStatistics(statistics)
                .setId(idUtil.getId())
                .setCourseId(courseId)
                .setDate(new Date(System.currentTimeMillis()))
                .setPeriod(period);
        rollStatisticsMapper.insert(rollStatistics);
        /*保存统计结果到redis中*/
        rollDataRedis.saveRollDataStatistics(courseId,statistics);
        return statistics;
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
