package com.system.roll.controller;

import com.system.roll.service.SupervisorBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supervisor/base")
public class SupervisorBaseController {

    @Autowired
    private SupervisorBaseService supervisorBaseService;

    @PostMapping("/course/upload")
    public void uploadCourse(){

    }

    @PostMapping("/course/update")
    public void updateCourse(){

    }

    @PostMapping("/course/delete")
    public void deleteCourse(@RequestParam("courseId") Long courseId){

    }

    @GetMapping("/course/getAll")
    public void getAllCourse(){

    }



}


