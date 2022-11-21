package com.system.roll.controller.sutdent;

import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.service.student.StudentBaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
        return studentBaseService.getAllCourse();
    }

    @PostMapping("/register")
    public StudentBaseService.InfoVo register(StudentBaseService.InfoDto infoDto){
        return studentBaseService.register(infoDto);
    }

}
