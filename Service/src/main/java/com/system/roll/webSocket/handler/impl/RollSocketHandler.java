package com.system.roll.webSocket.handler.impl;

import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.vo.Result;
import com.system.roll.entity.vo.roll.SingleRollStatisticVo;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.entity.properites.RabbitProperties;
import com.system.roll.rabbit.utils.RabbitUtil;
import com.system.roll.security.interceptor.LoginInterceptor;
import com.system.roll.service.supervisor.SupervisorRollService;
import com.system.roll.utils.SpringContextUtil;
import com.system.roll.webSocket.context.SocketContext;
import com.system.roll.webSocket.context.SocketContextHandler;
import com.system.roll.webSocket.handler.CustomHttpServletRequest;
import com.system.roll.webSocket.handler.SocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;

/**
 * 督导人员端发送点名数据的长连接
 * */
@Slf4j
@Component
@ServerEndpoint(value = "/supervisor/roll/call/{socketId}/{token}")
public class RollSocketHandler implements SocketHandler {

    private String socketId;
    private Integer enrollNum;//应点名的人数
    private Integer count;//计数器
    RabbitUtil rabbitUtil;
    RabbitProperties rabbitProperties;

    @Override
    public void open(Session session, String socketId) {}

    /**
     * 建立督导队员发送点名考勤数据的长连接
     * @param session 会话对象
     * @param socketId 这里定义为courseId
     * @param token 身份令牌
     * */
    @OnOpen
    @Override
    public void open(Session session, @PathParam(value = "socketId") String socketId,@PathParam(value = "token") String token) throws IOException, EncodeException {
        /*进行token验证*/
        LoginInterceptor loginInterceptor = SpringContextUtil.getBean("LoginInterceptor");
        try {loginInterceptor.preHandle(new CustomHttpServletRequest().setHeader("Authorization",token).setSession(session),new Response(),token);}
        catch (ServiceException e){
            session.getBasicRemote().sendObject(Result.error(e));
            session.close();
        }
        /*手动注入组件*/
        rabbitUtil = SpringContextUtil.getBean("RabbitUtil");
        rabbitProperties = SpringContextUtil.getBean("RabbitProperties");
        this.socketId = socketId;
        /*将自己注册到context中*/
        SocketContextHandler.addContext("roll:"+socketId,new SocketContext(this,session));
    }

    @OnMessage
    @Override
    public void onMessage(String data) throws Exception {
        if (data.matches("\\d+")) {
            this.enrollNum = Integer.valueOf(data);
            this.count = -1;
        }
        rabbitUtil.sendMessage(rabbitProperties.getWsExchange(),rabbitProperties.getRollDataSource(),this.socketId+","+data);
        this.count++;
        /*完成点名*/
        if (Objects.equals(this.count, this.enrollNum)){
            /*调用业务方法，获取统计数据*/
            try {
                SupervisorRollService supervisorRollService = SpringContextUtil.getBean("SupervisorRollService");
                SingleRollStatisticVo rollDataStatistic = supervisorRollService.getRollDataStatistic(this.socketId);
                SocketContextHandler.getContext("roll:"+socketId).sendMessage(ResultCode.SUCCESS,rollDataStatistic);
            }catch (Exception e){
                SocketContextHandler.getContext("roll:"+socketId).sendMessage(ResultCode.SERVER_ERROR,null);
            }
        }
    }

    @OnClose
    @OnError
    @Override
    public void onClose(@PathParam(value = "socketId") String socketId) throws IOException {
        SocketHandler.super.onClose("roll:"+socketId);
    }
}
