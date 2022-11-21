package com.system.roll.controller.auth;

import com.system.roll.constant.impl.ResultCode;
import com.system.roll.constant.impl.Role;
import com.system.roll.entity.vo.professor.ProfessorVo;
import com.system.roll.entity.vo.student.StudentVo;
import com.system.roll.entity.vo.supervisor.SupervisorVo;
import com.system.roll.exception.impl.ServiceException;
import com.system.roll.security.jwt.JwtSecurityHandler;
import com.system.roll.service.AuthService;
import com.system.roll.utils.IdUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 负责学生端、督导队员端的授权登录
 * 负责web端的第三方授权验证
 * */
@Slf4j
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Resource
    private AuthService authService;
    @Resource
    private JwtSecurityHandler jwtSecurityHandler;
    @Resource
    private IdUtil idUtil;

    /**
     * 学生端微信小程序登录
     * */
    @PostMapping(value = "/login/student")
    public StudentVo studentLogin(@RequestBody LoginFormDto data, HttpServletResponse response){
        /*获取信息*/
        StudentVo studentVo = authService.studentLogin(data.getCode());
        /*生成token*/
        String token = jwtSecurityHandler.getToken(studentVo.getId(),studentVo.getName(), Role.STUDENT.getCode(), studentVo.getDepartmentId());
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
        String token = jwtSecurityHandler.getToken(supervisorVo.getId(),supervisorVo.getName(), supervisorVo.getRole(), supervisorVo.getDepartmentId());
        /*写入token*/
        response.setHeader("Authorization",token);
        /*返回信息*/
        return supervisorVo;
    }

    /**
     * 督导队员端小程序对web端登录进行第三方授权（由督导队员调用）
     * */
    @PostMapping(value = "/login/professor")
    public ProfessorVo professorLogin(@RequestBody LoginFormDto data,HttpServletResponse response){
        /*获取信息*/
        ProfessorVo professorVo = authService.professorLogin(data.getSocketId(),data.getCode());
        /*生成token*/
        String token = jwtSecurityHandler.getToken(professorVo.getId(),professorVo.getName(), professorVo.getRole(), professorVo.getDepartmentId());
        /*写入token*/
        response.setHeader("Authorization",token);
        /*返回信息*/
        return professorVo;
    }

    /**
     * 生成第三方授权码
     * */
    @GetMapping(value = "/login/QRCode")
    public void professorLogin(HttpServletResponse response){
        /*分配socketId*/
        String socketId = idUtil.getWebSocketId();
        /*调用微信小程序接口，获取图片流（字符串形式）*/
        String stream = authService.generateQRCode(socketId);
        /*将图片流字符串写入流中，并将分配的socketId写入响应头中*/
        try {
            response.setContentType("image/jpeg");
            response.setHeader("Cache-Control","no-cache, must-revalidate");
//            response.setHeader("Content-disposition","attachment; filename=\"_123456789\"");
            response.setHeader("Content-disposition","inline");
            response.setHeader("Content-Length", String.valueOf(stream.length()));
            response.setHeader("SocketId",socketId);//自定义响应头
            PrintWriter writer = response.getWriter();
            writer.write(stream);
            writer.close();
        } catch (IOException e) {
            log.error(e.getCause().toString());
            throw new ServiceException(ResultCode.FAILED_TO_RESPONSE_RESOURCE);
        }
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
