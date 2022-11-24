package com.system.roll.controller.professor;

import com.system.roll.entity.vo.roll.RollDataVo;
import com.system.roll.entity.vo.roll.TotalRollStatisticVo;
import com.system.roll.entity.vo.student.StudentRollDataListVo;
import com.system.roll.entity.vo.student.StudentRollRecord;
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
    public RollDataVo getRollData(@RequestParam("courseId") Long courseId){
        return professorRollService.getRollData(courseId);
    }

    @GetMapping("/roll/classMembers/getAll")
    public StudentRollDataListVo getClassMembers(@RequestParam("courseId")   Long courseId,
                                                 @RequestParam("sortRule")   Integer sortRule){
        return professorRollService.getClassMembers(courseId,sortRule);
    }

    @GetMapping("/classMembers/query")
    public StudentRollRecord getOneClassMember(@RequestParam("courseId")     Long courseId,
                                               @RequestParam("studentId")    Long studentId){
        return professorRollService.getOneClassMember(courseId,studentId);
    }

    @GetMapping("/rollData/statistic")
    public TotalRollStatisticVo getStatistic(@RequestParam("courseId")   Long courseId,
                                             @RequestParam("sortRole")   Integer sortRole){
        return professorRollService.getStatistic(courseId,sortRole);
    }

    @GetMapping("/roll/output/normal")
    public void getNormalOutput(@RequestParam("courseId")   Long courseId,
                                @RequestParam("sortRole")   Integer sortRole){

    }

    @GetMapping("/roll/output/custom")
    public void getCustomOutput(){

    }

}
