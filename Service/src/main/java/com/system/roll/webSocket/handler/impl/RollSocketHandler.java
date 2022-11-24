package com.system.roll.webSocket.handler.impl;

import com.system.roll.context.security.SecurityContext;
import com.system.roll.context.security.SecurityContextHolder;
import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.entity.properites.RabbitProperties;
import com.system.roll.entity.vo.Result;
import com.system.roll.entity.vo.roll.SingleRollStatisticVo;
import com.system.roll.rabbit.utils.RabbitUtil;
import com.system.roll.security.interceptor.LoginInterceptor;
import com.system.roll.service.supervisor.SupervisorRollService;
import com.system.roll.utils.JsonUtil;
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
@ServerEndpoint(value = "/supervisor/roll/call/{courseId}/{enrollNum}/{token}")
public class RollSocketHandler implements SocketHandler {

    private String courseId;
    private Integer enrollNum;//应点名的人数
    private Integer count;//计数器
    private RabbitUtil rabbitUtil;
    private RabbitProperties rabbitProperties;
    private SecurityContext securityContext;

    @Override
    public void open(Session session, String courseId) {}

    @Override
    public void open(Session session, String param, String token) throws IOException, EncodeException {

    }

    /**
     * 建立督导队员发送点名考勤数据的长连接
     * @param session 会话对象
     * @param courseId 这里定义为courseId
     * @param token 身份令牌
     * */
    @OnOpen
    public void open(Session session, @PathParam(value = "courseId") String courseId,
                     @PathParam(value = "enrollNum") String enrollNum,
                     @PathParam(value = "token") String token) throws IOException, EncodeException {
        /*进行token验证*/
        LoginInterceptor loginInterceptor = SpringContextUtil.getBean("LoginInterceptor");
        try {loginInterceptor.preHandle(new CustomHttpServletRequest().setHeader("Authorization",token).setSession(session),new Response(),token);}
        catch (ServiceException e){
            session.getBasicRemote().sendText(JsonUtil.toJson(Result.error(e)));
            session.close();
        }
        System.out.println(SecurityContextHolder.getContext().getAuthorization().getInfo(String.class, "id"));
        /*手动注入组件*/
        rabbitUtil = SpringContextUtil.getBean("RabbitUtil");
        rabbitProperties = SpringContextUtil.getBean("RabbitProperties");
        this.courseId = courseId;
        this.enrollNum = Integer.valueOf(enrollNum);
        this.count = 0;
        /*将自己注册到context中*/
        SocketContextHandler.addContext("roll:"+courseId,new SocketContext(this,session));
        this.securityContext = SecurityContextHolder.getContext();
        SecurityContextHolder.clearContext();
        /*发送点名人数信息*/
        rabbitUtil.sendMessage(rabbitProperties.getWsExchange(),rabbitProperties.getRollDataSource(),this.courseId+","+enrollNum);
    }

    @OnMessage
    @Override
    public void onMessage(String data) throws Exception {
        rabbitUtil.sendMessage(rabbitProperties.getWsExchange(),rabbitProperties.getRollDataSource(),this.courseId+","+data);
        this.count++;
        /*完成点名*/
        if (Objects.equals(this.count, this.enrollNum)){
            /*更新环境类的映射（在需要使用到环境类的线程中，需要更新一下环境类的映射）*/
            SecurityContextHolder.setContext(securityContext);
            System.out.println("1:"+Thread.currentThread().getId());
            /*调用业务方法，获取统计数据*/
            try {
                SupervisorRollService supervisorRollService = SpringContextUtil.getBean("SupervisorRollService");
                SingleRollStatisticVo rollDataStatistic = supervisorRollService.getRollDataStatistic(this.courseId);
                SocketContextHandler.getContext("roll:"+courseId).sendMessage(ResultCode.SUCCESS,rollDataStatistic);
            }catch (Exception e){
                e.printStackTrace();
                SocketContextHandler.getContext("roll:"+courseId).sendMessage(ResultCode.SERVER_ERROR,null);
            }
        }
    }

    @OnClose
    @OnError
    @Override
    public void onClose(@PathParam(value = "courseId") String courseId) throws IOException {
        SocketHandler.super.onClose("roll:"+courseId);
    }
}
