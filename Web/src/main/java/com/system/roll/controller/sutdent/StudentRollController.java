package com.system.roll.controller.sutdent;

import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.dto.student.PositionDto;
import com.system.roll.entity.vo.leave.LeaveListVo;
import com.system.roll.entity.vo.leave.LeaveVo;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.entity.vo.student.RollHistoryVo;
import com.system.roll.service.student.StudentRollService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.Map;

@RestController
@RequestMapping("/student/roll")
public class StudentRollController {
    @Resource
    private StudentRollService studentRollService;

    @PostMapping("/getNow")
    public void getNow(@RequestBody Map<String,Object> data){
        if(!data.containsKey("courseId")) throw new ServiceException(ResultCode.BODY_NOT_MATCH);
        String courseId = String.valueOf(data.get("courseId"));
    }

    @PostMapping("/putPosition")
    public void putPosition(@RequestBody PositionDto data){
        /*上传经纬度信息，进行判断*/
        studentRollService.putPosition(data);
    }

    @PostMapping("/leave/apply")
    public void applyLeave(@RequestBody StudentRollService.LeaveQueryDTO leaveDto){
        studentRollService.applyQuery(leaveDto);
    }

    @GetMapping("/leave/query")
    public LeaveVo leaveQuery(@RequestParam("leaveId") String leaveId){
        return studentRollService.leaveQuery(leaveId);
    }

    @GetMapping("/leave/getAll")
    public LeaveListVo getAllLeave(){
        return studentRollService.getAllLeave();
    }

    @PostMapping("/message/getAll")
    public void getAllMessage(){

    }

    @GetMapping("/history")
    public RollHistoryVo getHistory(@RequestParam("date")Date date){
        return studentRollService.getHistory(date);
    }


}
