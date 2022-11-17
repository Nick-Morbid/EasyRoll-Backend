package com.system.roll.service;

import com.system.roll.constant.impl.OperationType;
import com.system.roll.describer.annotation.Operation;
import com.system.roll.utils.LoggerUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TestService {

    @Resource(name = "LoggerUtil")
    private LoggerUtil loggerUtil;

    @Operation(type = OperationType.UPDATE_COURSE)
    public void uploadCourse(){
        loggerUtil.info("课程名称为：<<软件工程>>");
        loggerUtil.info("授课老师为：KEX");
        loggerUtil.info("课程起止时间为：2-14周");
        loggerUtil.info("课程人数：139");
    }
}
