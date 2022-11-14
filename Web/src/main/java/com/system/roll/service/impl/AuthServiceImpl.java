package com.system.roll.service.impl;

import com.system.roll.constant.impl.ResultCode;
import com.system.roll.controller.AuthController;
import com.system.roll.entity.bo.JsCode2sessionBo;
import com.system.roll.entity.vo.professor.ProfessorVo;
import com.system.roll.entity.vo.student.StudentVo;
import com.system.roll.entity.vo.supervisor.SupervisorVo;
import com.system.roll.exception.impl.ServiceException;
import com.system.roll.service.AuthService;
import com.system.roll.service.StudentInfoService;
import com.system.roll.service.SupervisorInfoService;
import com.system.roll.service.WxApiService;
import com.system.roll.service.professor.ProfessorInfoService;
import com.system.roll.utils.JsonUtil;
import com.system.roll.webSocket.context.SocketContextHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.EncodeException;
import java.io.IOException;

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
        SupervisorVo supervisorInfo = supervisorInfoService.getSupervisorInfo(openId);
        if (supervisorInfo==null) throw new ServiceException(ResultCode.NOT_REGISTER);
        return supervisorInfo;
    }

    @Override
    public ProfessorVo webLogin(String socketId, String code) {
        /*根据code获取用户唯一的标识*/
        JsCode2sessionBo jsCode2sessionBo = wxApiService.jsCode2session(code);
        String openId = jsCode2sessionBo.getOpenId();
        /*根据openId获取教师/辅导员信息*/
        ProfessorVo professorVo = professorInfoService.getProfessorInfo(openId);
        if (professorVo==null) throw new ServiceException(ResultCode.NOT_REGISTER);
        /*调用长连接，通知web端登录成功*/
        try {
            SocketContextHandler.getContext(socketId).getSocketHandler().sendMessage(JsonUtil.toJson(professorVo));
            SocketContextHandler.getContext(socketId).getSocketHandler().close();//关闭长连接
        }
        catch (IOException | EncodeException e) { throw new ServiceException(ResultCode.WEBSOCKET_SEND_FAILED);}
        /*返回信息*/
        return professorVo;
    }

    @Override
    public StudentVo studentRegister(AuthController.RegisterFormDto data) {
        return null;
    }

    @Override
    public SupervisorVo supervisorRegister(AuthController.RegisterFormDto data) {
        return null;
    }

    @Override
    public ProfessorVo professorRegister(AuthController.RegisterFormDto data) {
        return null;
    }
}
