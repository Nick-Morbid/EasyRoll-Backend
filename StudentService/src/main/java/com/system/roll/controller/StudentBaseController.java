package com.system.roll.controller;

import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.service.StudentBaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/student/base")
public class StudentBaseController {
    @Resource
    private StudentBaseService studentBaseService;

    @GetMapping("/course/getAll")
    public CourseListVo getAllCourse(){
        return null;
    }
}
