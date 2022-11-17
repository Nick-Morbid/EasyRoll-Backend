package com.system.roll.service.impl;

import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.mapper.CourseMapper;
import com.system.roll.security.context.SecurityContextHolder;
import com.system.roll.service.StudentBaseService;
import com.system.roll.utils.DateUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Date;

@Component(value = "studentBaseService")
public class StudentBaseServiceImpl implements StudentBaseService {
    @Resource
    private DateUtil dateUtil;

    @Resource
    private CourseMapper courseMapper;

    @Override
    public CourseListVo getAllCourse() {
        String studentId = SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "id");
        Integer currentWeek = dateUtil.getWeek(new Date(System.currentTimeMillis()));
        Integer currentDay = dateUtil.getWeekDay(new Date(System.currentTimeMillis()));

        return null;
    }
}
