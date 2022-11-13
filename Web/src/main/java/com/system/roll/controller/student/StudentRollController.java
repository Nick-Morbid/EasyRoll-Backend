package com.system.roll.controller.student;

import com.system.roll.service.student.StudentRollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student/roll")
public class StudentRollController {
    @Autowired
    private StudentRollService studentRollService;

    @PostMapping("/getNow")
    public void getNow(@RequestParam("courseId") Long courseId){

    }

    @PostMapping("/putPosition")
    public void putPosition(){

    }

    @PostMapping("/leave/apply")
    public void applyLeave(){

    }

    @GetMapping("/leave/query")
    public void leaveQuery(@RequestParam("leaveId") Long leaveId){

    }

    @GetMapping("/leave/getAll")
    public void getAllLeave(){

    }

    @PostMapping("/message/getAll")
    public void getAllMessage(){

    }
}
