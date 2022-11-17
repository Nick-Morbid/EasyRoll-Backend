package com.system.roll.controller;

import com.system.roll.entity.vo.student.StudentRollListVo;
import com.system.roll.service.SupervisorRollService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/supervisor/roll")
public class SupervisorRollController {

    @Autowired
    private SupervisorRollService supervisorRollService;

    @PostMapping("/roll/publish")
    public void publishRoll(@RequestBody RollDto data){
        supervisorRollService.publishRoll(data);
    }

    @GetMapping("/getForm")
    public StudentRollListVo getForm(@RequestParam("courseId") Long courseId){
        return null;
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class RollDto{
        private Timestamp startTime;
        private Timestamp endTime;
        private String courseId;
    }


}
