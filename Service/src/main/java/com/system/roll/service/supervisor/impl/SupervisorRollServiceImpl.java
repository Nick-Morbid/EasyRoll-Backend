package com.system.roll.service.supervisor.impl;

import com.system.roll.describer.annotation.Operation;
import com.system.roll.entity.constant.impl.OperationType;
import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.entity.pojo.Student;
import com.system.roll.entity.vo.message.MessageListVo;
import com.system.roll.entity.vo.roll.SingleRollStatisticVo;
import com.system.roll.entity.vo.student.StudentRollListVo;
import com.system.roll.handler.mapstruct.StudentConvertor;
import com.system.roll.mapper.StudentMapper;
import com.system.roll.redis.RollDataRedis;
import com.system.roll.redis.StudentRedis;
import com.system.roll.service.supervisor.SupervisorRollService;
import com.system.roll.utils.DateUtil;
import com.system.roll.utils.EnumUtil;
import com.system.roll.utils.PinyinUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Date;
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

    @Resource
    private DateUtil dateUtil;
    @Override
    @Operation(type = OperationType.TAKE_A_ROLL)
    public SingleRollStatisticVo getRollDataStatistic(String courseId) throws InterruptedException {
        /*获取redis中的记录*/
        SingleRollStatisticVo statistics = null;
        int count = 1000;
        Date date = new Date(System.currentTimeMillis());
        while (!(count-- <= 0)){
            statistics = rollDataRedis.getRollDataStatistics(courseId);
            if (statistics.getDate().getTime()-date.getTime()< 3600*24) break;
            Thread.sleep(100);
        }
        if (statistics.getDate().getTime()-date.getTime()>= 3600*24) throw new ServiceException(ResultCode.SERVER_ERROR);
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
}
