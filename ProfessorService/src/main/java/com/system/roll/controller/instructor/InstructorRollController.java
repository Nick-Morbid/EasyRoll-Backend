package com.system.roll.controller.instructor;

import com.system.roll.service.instructor.InstructorRollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/instruct")
public class InstructorRollController {

    @Autowired
    private InstructorRollService instructorRollService;

    @GetMapping("/rollData/getAll")
    public void getRollData(@RequestParam("date")Date date){

    }

    @GetMapping("/rollData/statistic")
    public void getStatistic(@RequestParam("courseId")  Long courseId,
                             @RequestParam("classNo")   Integer classNo){

    }

    @GetMapping("/roll/output")
    public void getOutput(@RequestParam("courseId")     Long courseId,
                          @RequestParam("classNo")      Integer classNo,
                          @RequestParam("outputRole")   Integer outputRole){

    }
}
