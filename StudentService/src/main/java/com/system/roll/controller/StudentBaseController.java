package com.system.roll.controller;

import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.service.StudentBaseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
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
    public InfoVo register(InfoDto infoDto){
        return studentBaseService.register(infoDto);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class InfoVo{
        private String id;
        private String name;
        private String departmentId;
        private String departmentName;
        private String majorId;
        private String major;
        private Integer grade;
        private Integer classNo;
        private Integer currentWeek;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class InfoDto{
        private String name;
        private String departmentId;
        private String majorId;
        private Integer role;
        private String openId;
        private Integer grade;
        private Integer classNo;
    }
}
