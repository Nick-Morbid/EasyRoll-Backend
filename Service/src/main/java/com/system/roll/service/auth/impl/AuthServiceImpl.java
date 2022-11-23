package com.system.roll.service.auth.impl;

import com.system.roll.entity.bo.JsCode2sessionBo;
import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.entity.vo.professor.ProfessorVo;
import com.system.roll.entity.vo.student.InfoVo;
import com.system.roll.entity.vo.supervisor.SupervisorVo;
import com.system.roll.service.auth.AuthService;
import com.system.roll.service.auth.WxApiService;
import com.system.roll.service.professor.ProfessorBaseService;
import com.system.roll.service.student.StudentBaseService;
import com.system.roll.service.supervisor.SupervisorBaseService;
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
    @Resource(name = "StudentBaseService")
    private StudentBaseService studentBaseService;
    @Resource(name = "SupervisorBaseService")
    private SupervisorBaseService supervisorBaseService;
    @Resource(name = "ProfessorBaseService")
    private ProfessorBaseService professorBaseService;


    @Override
    public InfoVo studentLogin(String code) {
        /*根据code获取用户唯一标识*/
        JsCode2sessionBo jsCode2sessionBo = wxApiService.jsCode2session(code);
        String openId = jsCode2sessionBo.getOpenId();
        /*根据openId获取学生信息*/
        InfoVo infoVo = studentBaseService.getStudentInfo(openId);
        if (infoVo==null) throw new ServiceException(ResultCode.NOT_REGISTER);
        return infoVo;
    }

    @Override
    public SupervisorVo supervisorLogin(String code) {
        /*根据code获取用户唯一标识*/
        JsCode2sessionBo jsCode2sessionBo = wxApiService.jsCode2session(code);
        String openId = jsCode2sessionBo.getOpenId();
        /*根据openId获取督导人员信息*/
        SupervisorVo supervisorInfo = supervisorBaseService.getSupervisorInfo(openId);
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
        ProfessorVo professorVo = professorBaseService.getProfessorInfo(openId);
//        ProfessorVo professorVo = new ProfessorVo().setId("123456789").setName("Kex").setDepartmentId("123").setDepartmentName("计算机与大数据学院").setRole(Role.PROFESSOR.getCode());
        /*调用长连接，通知web端登录成功*/
        try {
            if (professorVo!=null) SocketContextHandler.getContext(socketId).sendMessage(ResultCode.SUCCESS,professorVo);
            else SocketContextHandler.getContext(socketId).sendMessage(ResultCode.NOT_REGISTER,null);
            SocketContextHandler.clearContext(socketId);//销毁websocket上下文
        }
        catch (IOException | EncodeException e) {
            e.printStackTrace();
            throw new ServiceException(ResultCode.WEBSOCKET_SEND_FAILED);}
        if (professorVo==null) throw new ServiceException(ResultCode.NOT_REGISTER);

        /*返回信息*/
        return professorVo;
    }

}
