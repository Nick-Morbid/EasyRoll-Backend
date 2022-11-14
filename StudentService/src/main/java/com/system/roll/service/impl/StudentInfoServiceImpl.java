package com.system.roll.service.impl;

import com.system.roll.entity.vo.student.StudentVo;
import com.system.roll.service.StudentInfoService;
import org.springframework.stereotype.Component;

@Component(value = "StudentInfoService")
public class StudentInfoServiceImpl implements StudentInfoService {
    @Override
    public StudentVo getStudentInfo(String openId) {
        return null;
    }
}
