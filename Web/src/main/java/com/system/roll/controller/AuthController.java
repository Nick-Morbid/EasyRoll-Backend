package com.system.roll.controller;

import com.system.roll.constant.impl.Role;
import com.system.roll.entity.vo.professor.ProfessorVo;
import com.system.roll.entity.vo.student.StudentVo;
import com.system.roll.entity.vo.supervisor.SupervisorVo;
import com.system.roll.security.jwt.JwtSecurityHandler;
import com.system.roll.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 负责学生端、督导队员端的授权登录
 * 负责web端的第三方授权验证
 * */
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Resource
    private AuthService authService;
    @Resource
    private JwtSecurityHandler jwtSecurityHandler;

    /**
     * 学生端微信小程序登录
     * */
    @PostMapping(value = "/login/student")
    public StudentVo studentLogin(@RequestBody LoginFormDto data, HttpServletResponse response){
        /*获取信息*/
        StudentVo studentVo = authService.studentLogin(data.getCode());
        /*生成token*/
        String token = jwtSecurityHandler.getToken(studentVo.getId(), Role.STUDENT.getCode(), studentVo.getDepartmentId());
        /*写入token*/
        response.setHeader("Authorization",token);
        /*返回信息*/
        return studentVo;
    }

    /**
     * 督导队员端微信小程序登录
     * */
    @PostMapping(value = "/login/supervisor")
    public SupervisorVo supervisorLogin(@RequestBody LoginFormDto data,HttpServletResponse response){
        /*获取信息*/
        SupervisorVo supervisorVo = authService.supervisorLogin(data.getCode());
        /*生成token*/
        String token = jwtSecurityHandler.getToken(supervisorVo.getId(), supervisorVo.getRole(), supervisorVo.getDepartmentId());
        /*写入token*/
        response.setHeader("Authorization",token);
        /*返回信息*/
        return supervisorVo;
    }

    /**
     * 生成第三方授权码
     * */
    @PostMapping(value = "/login/QRCode")
    public void professorLogin(){

    }

    /**
     * 督导队员端小程序对web端登录进行第三方授权（由督导队员调用）
     * */
    @PostMapping(value = "/login/professor")
    public ProfessorVo professorLogin(@RequestBody LoginFormDto data,HttpServletResponse response){
        /*获取信息*/
        ProfessorVo professorVo = authService.webLogin(data.getSocketId(),data.getCode());
        /*生成token*/
        String token = jwtSecurityHandler.getToken(professorVo.getId(), professorVo.getRole(), professorVo.getDepartmentId());
        /*写入token*/
        response.setHeader("Authorization",token);
        /*返回信息*/
        return professorVo;
    }

    /**
     * 学生端小程序注册（注册后直接完成登录）
     * */
    @PostMapping(value = "/register/student")
    public StudentVo studentRegister(@RequestBody RegisterFormDto data){
        return null;
    }

    /**
     * 督导队员端小程序注册（注册后直接完成登录）
     * */
    @PostMapping(value = "/register/supervisor")
    public SupervisorVo supervisorRegister(@RequestBody RegisterFormDto data){
        return null;
    }

    /**
     * 网页端注册（在督导队员端小程序完成注册，注册后直接完成登录）
     * */
    @PostMapping(value = "/register/professor")
    public ProfessorVo webRegister(@RequestBody RegisterFormDto data){
        return null;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class LoginFormDto{
        private String  socketId;
        private String code;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class RegisterFormDto{
        private String id;
        private String name;
        private String departmentName;
        private Integer role;//0：学生，1：督导队员，2：教师，3：辅导员
    }
}
