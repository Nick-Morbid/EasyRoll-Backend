package com.system.roll.service;

import com.system.roll.controller.auth.AuthController;
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
     * 微信小程序授权网页端登录（教师/辅导员登录）
     * */
    ProfessorVo professorLogin(String socketId, String code);

    /**
     * 生成QRCode码，并作为文件流发送给前端
     * @return 随机生成的socketId（前端用该socketId去建立长连接）
     * */
    String generateQRCode(String socketId);

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
