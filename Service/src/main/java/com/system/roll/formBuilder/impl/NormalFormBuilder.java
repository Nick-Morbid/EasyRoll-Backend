package com.system.roll.formBuilder.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.entity.pojo.CourseRelation;
import com.system.roll.entity.vo.student.StudentRollListVo;
import com.system.roll.formBuilder.FormBuilder;
import com.system.roll.mapper.CourseRelationMapper;
import com.system.roll.redis.StudentRedis;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 直接获取完整的点名表单
 * */
@Component(value = "FormBuilder")
public class NormalFormBuilder implements FormBuilder {

    @Resource
    private CourseRelationMapper courseRelationMapper;
    @Resource(name = "StudentRedis")
    private StudentRedis studentRedis;
    @Override
    public StudentRollListVo getForm(String courseId) {
        LambdaQueryWrapper<CourseRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseRelation::getCourseId,courseId);
        List<CourseRelation> courseRelations = courseRelationMapper.selectList(wrapper);
        if (courseRelations==null||courseRelations.size()==0) throw new ServiceException(ResultCode.RESOURCE_NOT_FOUND);
        StudentRollListVo studentRollListVo = new StudentRollListVo();
        List<StudentRollListVo.StudentRollVo> studentRollVoList = courseRelations.stream().map(courseRelation -> new StudentRollListVo.StudentRollVo().setId(courseRelation.getStudentId()).setName(courseRelation.getStudentName()).setPinyin(Arrays.asList(studentRedis.getPinYin(courseRelation.getStudentId())))).collect(Collectors.toList());
        studentRollListVo.setStudents(studentRollVoList).setTotal(studentRollVoList.size());
        return studentRollListVo;
    }
}
