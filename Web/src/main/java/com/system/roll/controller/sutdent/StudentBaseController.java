package com.system.roll.controller.sutdent;

import com.system.roll.entity.dto.student.InfoDto;
import com.system.roll.entity.vo.course.CourseListVo;
import com.system.roll.entity.vo.student.InfoVo;
import com.system.roll.service.student.StudentBaseService;
import org.springframework.web.bind.annotation.*;

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
    public InfoVo register(@RequestBody  InfoDto infoDto){
        return studentBaseService.register(infoDto);
    }

}
