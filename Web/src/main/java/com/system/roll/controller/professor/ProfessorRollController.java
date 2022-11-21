package com.system.roll.controller.professor;

import com.system.roll.service.professor.ProfessorRollService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/professor")
public class ProfessorRollController {

    @Resource(name = "ProfessorRollService")
    private ProfessorRollService professorRollService;

    @GetMapping("/rollData/getLatest")
    public void getRollData(@RequestParam("courseId") Long courseId){

    }

    @GetMapping("/roll/classMembers/getAll")
    public void getClassMembers(@RequestParam("courseId")   Long courseId,
                                @RequestParam("sortRole")   Integer sortRole){

    }

    @GetMapping("/classMembers/query")
    public void getOneClassMember(@RequestParam("courseId")     Long courseId,
                                  @RequestParam("studentId")    Long studentId){

    }

    @GetMapping("/rollData/statistic")
    public void getStatistic(@RequestParam("courseId")   Long courseId,
                             @RequestParam("sortRole")   Integer sortRole){

    }

    @GetMapping("/roll/output/normal")
    public void getNormalOutput(@RequestParam("courseId")   Long courseId,
                                @RequestParam("sortRole")   Integer sortRole){

    }

    @GetMapping("/roll/output/custom")
    public void getCustomOutput(){

    }

}
