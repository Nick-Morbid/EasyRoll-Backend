package com.system.roll.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.system.roll.constant.impl.ResultCode;
import com.system.roll.entity.vo.leave.LeaveListVo;
import com.system.roll.entity.vo.leave.LeaveVo;
import com.system.roll.exception.impl.ServiceException;
import com.system.roll.service.StudentRollService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
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
    public void putPosition(){
    }

    @PostMapping("/leave/apply")
    public void applyLeave(@RequestBody LeaveQueryDTO leaveDto){
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    @ToString
    public static class LeaveQueryDTO{
        private Timestamp startTime;
        private Timestamp endTime;
        private String excuse;
        private String attachment;
    }
}
