package com.system.roll.controller;

import com.system.roll.constant.impl.Period;
import com.system.roll.entity.pojo.User;
import com.system.roll.entity.vo.course.CourseListVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/test")
public class TestController {
    @GetMapping("/test01")
    public User test01(){
        return new User().setId("1").setName("nick").setPassword("123");
    }

    @GetMapping("/test02")
    public CourseListVo test02(){
        List<CourseListVo.CourseVo> courses = new ArrayList<>();
        courses.add(new CourseListVo.CourseVo().setName("软件工程").setProfessorName("Kex").setStartWeek(2).setEndWeek(14).setId(1L).setEnrollNum(139).setPeriod(Period.EIGHT_TO_TEN.getMsg()));
        return new CourseListVo().setCourses(courses).setTotal(courses.size());
    }
}
