package com.system.roll.service.impl;

import com.system.roll.constant.impl.ResultCode;
import com.system.roll.constant.impl.Role;
import com.system.roll.controller.AuthController;
import com.system.roll.entity.bo.JsCode2sessionBo;
import com.system.roll.entity.pojo.Student;
import com.system.roll.entity.vo.professor.ProfessorVo;
import com.system.roll.entity.vo.student.StudentVo;
import com.system.roll.entity.vo.supervisor.SupervisorVo;
import com.system.roll.exception.impl.ServiceException;
import com.system.roll.service.AuthService;
import com.system.roll.service.StudentInfoService;
import com.system.roll.service.SupervisorInfoService;
import com.system.roll.service.WxApiService;
import com.system.roll.service.professor.ProfessorInfoService;
import com.system.roll.utils.IdUtil;
import com.system.roll.webSocket.context.SocketContextHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.EncodeException;
import java.io.IOException;

@Slf4j
@Service(value = "AuthService")
public class AuthServiceImpl implements AuthService {
    @Resource(name = "WxApiService")
    private WxApiService wxApiService;
    @Resource(name = "StudentInfoService")
    private StudentInfoService studentInfoService;
    @Resource(name = "SupervisorInfoService")
    private SupervisorInfoService supervisorInfoService;
    @Resource(name = "ProfessorInfoService")
    private ProfessorInfoService professorInfoService;
    @Resource
    private IdUtil idUtil;

    @Override
    public StudentVo studentLogin(String code) {
        /*根据code获取用户唯一标识*/
        JsCode2sessionBo jsCode2sessionBo = wxApiService.jsCode2session(code);
        String openId = jsCode2sessionBo.getOpenId();
        /*根据openId获取学生信息*/
        StudentVo studentInfo = studentInfoService.getStudentInfo(openId);
        if (studentInfo==null) throw new ServiceException(ResultCode.NOT_REGISTER);
        return studentInfo;
    }

    @Override
    public SupervisorVo supervisorLogin(String code) {
        /*根据code获取用户唯一标识*/
        JsCode2sessionBo jsCode2sessionBo = wxApiService.jsCode2session(code);
        String openId = jsCode2sessionBo.getOpenId();
        /*根据openId获取督导人员信息*/
//        SupervisorVo supervisorInfo = supervisorInfoService.getSupervisorInfo(openId);
        SupervisorVo supervisorInfo = new SupervisorVo().setId(openId).setName("nick").setRole(Role.STUDENT.getCode()).setDepartmentId("123456789").setDepartmentName("计算机与大数据学院");
        if (supervisorInfo==null) throw new ServiceException(ResultCode.NOT_REGISTER);
        return supervisorInfo;
    }

    @Override
    public String generateQRCode(String socketId) {
        /*获取access_token*/
        String accessToken = wxApiService.accessToken();
        /*获取图片流（这里是字符串形式）并返回*/
        return wxApiService.getGetWXACodeUnLimit(socketId,accessToken);
    }

    @Override
    public ProfessorVo professorLogin(String socketId, String code) {
        /*根据code获取用户唯一的标识*/
        JsCode2sessionBo jsCode2sessionBo = wxApiService.jsCode2session(code);
        String openId = jsCode2sessionBo.getOpenId();
        /*根据openId获取教师/辅导员信息*/
//        ProfessorVo professorVo = professorInfoService.getProfessorInfo(openId);
        ProfessorVo professorVo = new ProfessorVo().setId("123456789").setName("Kex").setDepartmentId("123").setDepartmentName("计算机与大数据学院").setRole(Role.PROFESSOR.getCode());
        /*调用长连接，通知web端登录成功*/
        try {
            if (professorVo!=null) SocketContextHandler.getContext(socketId).sendMessage(ResultCode.SUCCESS,professorVo);
            else SocketContextHandler.getContext(socketId).sendMessage(ResultCode.NOT_REGISTER,null);
            SocketContextHandler.clearContext(socketId);//销毁websocket上下文
        }
        catch (IOException | EncodeException e) { throw new ServiceException(ResultCode.WEBSOCKET_SEND_FAILED);}
        if (professorVo==null) throw new ServiceException(ResultCode.NOT_REGISTER);

        /*返回信息*/
        return professorVo;
    }

    @Override
    public StudentVo studentRegister(AuthController.RegisterFormDto data) {

        Student student = new Student();
//        return studentInfoService.register();
        return null;
    }

    @Override
    public SupervisorVo supervisorRegister(AuthController.RegisterFormDto data) {
//        return supervisorInfoService.register();
        return null;
    }

    @Override
    public ProfessorVo professorRegister(AuthController.RegisterFormDto data) {
//        return professorInfoService.register();
        return null;
    }
}
