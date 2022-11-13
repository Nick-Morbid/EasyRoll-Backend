package com.system.roll.controller;

import com.system.roll.service.SupervisorRollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supervisor/roll")
public class SupervisorRollController {

    @Autowired
    private SupervisorRollService supervisorRollService;

    @PostMapping("/roll/publish")
    public void publishRoll(){

    }

    @GetMapping("/getForm")
    public void getForm(@RequestParam("courseId") Long courseId){

    }

    @GetMapping("/statistic")
    public void getStatistic(){

    }

    @GetMapping("/message/getAll")
    public void getAllMessage(){

    }

    @PostMapping("/message/solve")
    public void solveMessage(){

    }

    @GetMapping("/output/excel")
    public void outputExcel(@RequestParam("courseId") Long courseId){

    }

    @GetMapping("/output/text")
    public void outputText(@RequestParam("courseId") Long courseId){

    }


}
