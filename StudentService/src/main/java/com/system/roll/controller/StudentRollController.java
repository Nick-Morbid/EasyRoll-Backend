package com.system.roll.controller;

import com.system.roll.service.StudentRollService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/student/roll")
public class StudentRollController {
    @Resource
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
