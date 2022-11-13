package com.system.roll.controller.student;

import com.system.roll.service.student.StudentBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student/base")
public class StudentBaseController {
    @Autowired
    private StudentBaseService studentBaseService;

    @GetMapping("/course/getAll")
    public void getAllCourse(){

    }
}
