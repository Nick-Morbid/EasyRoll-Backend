package com.system.roll.service;

import com.system.roll.controller.AuthController;
import com.system.roll.entity.vo.professor.ProfessorVo;
import com.system.roll.entity.vo.student.StudentVo;
import com.system.roll.entity.vo.supervisor.SupervisorVo;

public interface AuthService {
    /**
     * 学生端授权登录
     * */
    StudentVo studentLogin(String code);

    /**
     * 督导队员端授权登录
     * */
    SupervisorVo supervisorLogin(String code);
    /**
     * 网页端+微信小程序授权登录
     * */
    ProfessorVo webLogin(String socketId, String code);

    /**
     * 学生端注册
     * */
    StudentVo studentRegister(AuthController.RegisterFormDto data);

    /**
     * 督导人员注册
     * */
    SupervisorVo supervisorRegister(AuthController.RegisterFormDto data);

    /**
     * 教师和辅导员注册
     * */
    ProfessorVo professorRegister(AuthController.RegisterFormDto data);

}
