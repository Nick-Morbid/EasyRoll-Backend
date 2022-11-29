package com.system.roll.controller.auth;

import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.constant.impl.Role;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.entity.vo.professor.ProfessorVo;
import com.system.roll.entity.vo.student.InfoVo;
import com.system.roll.entity.vo.supervisor.SupervisorVo;
import com.system.roll.security.jwt.JwtSecurityHandler;
import com.system.roll.service.auth.AuthService;
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
    public InfoVo studentLogin(@RequestBody AuthService.LoginFormDto data, HttpServletResponse response){
        /*获取信息*/
        InfoVo infoVo = authService.studentLogin(data.getCode());
        /*生成token*/
        String token = jwtSecurityHandler.getToken(infoVo.getId(),infoVo.getName(), Role.STUDENT.getCode(), infoVo.getDepartmentId());
        /*写入token*/
        response.setHeader("Authorization",token);
        /*返回信息*/
        return infoVo;
    }

    /**
     * 督导队员端微信小程序登录
     * */
    @PostMapping(value = "/login/supervisor")
    public SupervisorVo supervisorLogin(@RequestBody AuthService.LoginFormDto data, HttpServletResponse response){
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
    public ProfessorVo professorLogin(@RequestBody AuthService.LoginFormDto data, HttpServletResponse response){
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
     * 获取第三方授权码路径和socketId
     * */
    @GetMapping(value = "/login/QRCode")
    public QRCodeVo professorLogin(){
        /*分配socketId*/
        String socketId = idUtil.getWebSocketId();
        /*生成授权码的资源访问路径（相对路径）*/
        String QRCodeUrl = "/auth/login/QRCode/generate?socketId="+socketId;
        /*封装视图对象*/
        return new QRCodeVo().setQRCodeUrl(QRCodeUrl).setSocketId(socketId);
    }

    /**
     * 生成第三方授权码
     * */
    @GetMapping(value = "/login/QRCode/generate")
    public void professorLogin1(@RequestParam(value = "socketId")String socketId,HttpServletResponse response){
//        log.warn("(1)分配的socketId为：{}",socketId);
        /*调用微信小程序接口，获取图片流（字符串形式）*/
        String stream = authService.generateQRCode(socketId);
        /*将图片流字符串写入流中，并将分配的socketId写入响应头中*/
        try {
            response.setContentType("image/jpeg");
            response.setHeader("Cache-Control","no-cache, must-revalidate");
//            response.setHeader("Content-disposition","attachment; filename=\"_123456789\"");
            response.setHeader("Content-disposition","inline");
            response.setHeader("Content-Length", String.valueOf(stream.length()));
//            log.warn("(2)分配的socketId为：{}",socketId);
//            response.setHeader("socketid",socketId);//自定义响应头
            PrintWriter writer = response.getWriter();
            writer.write(stream);
            writer.close();
        } catch (IOException e) {
            log.error(e.getCause().toString());
            throw new ServiceException(ResultCode.FAILED_TO_RESPONSE_RESOURCE);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class QRCodeVo{
        private String QRCodeUrl;
        private String socketId;
    }
}
