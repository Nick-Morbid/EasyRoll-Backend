package com.system.roll.controller.supervisor;

import com.system.roll.entity.vo.message.MessageListVo;
import com.system.roll.entity.vo.roll.statistics.StatisticDetailVo;
import com.system.roll.entity.vo.roll.statistics.StatisticsVo;
import com.system.roll.entity.vo.student.StudentRollListVo;
import com.system.roll.service.supervisor.SupervisorRollService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.sql.Date;

@RestController
@RequestMapping("/supervisor/roll")
public class SupervisorRollController {

    @Resource(name = "SupervisorRollService")
    private SupervisorRollService supervisorRollService;

    @PostMapping("/roll/publish")
    public void publishRoll(@RequestBody SupervisorRollService.RollDto data){
        supervisorRollService.publishRoll(data);
    }

    @GetMapping("/getForm")
    public StudentRollListVo getForm(@RequestParam("courseId") String courseId){
        return supervisorRollService.getForm(courseId);
    }

    @GetMapping("/message/getAll")
    public MessageListVo getAllMessage(){
        return supervisorRollService.getMessageList();
    }

    @PostMapping("/message/solve")
    public void solveMessage(@RequestBody SupervisorRollService.SolveDto data){
        supervisorRollService.solveMessage(data);
    }

    @GetMapping("/output/excel")
    public void outputExcel(@RequestParam("courseId") String courseId){
        supervisorRollService.outputExcel(courseId);
    }

    @GetMapping("/output/text")
    public void outputText(@RequestParam("courseId") Long courseId){
        supervisorRollService.outputText(courseId);
    }

    @GetMapping("/statistics")
    public StatisticsVo getStatistics(@RequestParam(required = false,value = "weekDay") Date weekDay){
        return supervisorRollService.getStatistics(weekDay);
    }

    @GetMapping("/detail")
    public StatisticDetailVo getStatisticDetail(@RequestParam("statisticsId") String statisticsId){
        return supervisorRollService.getStatisticDetail(statisticsId);
    }


}
