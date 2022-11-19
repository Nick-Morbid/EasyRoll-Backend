package com.system.roll.webSocket.handler.impl;

import com.system.roll.constant.impl.ResultCode;
import com.system.roll.constant.impl.TimeUnit;
import com.system.roll.entity.pojo.RollData;
import com.system.roll.entity.vo.Result;
import com.system.roll.exception.impl.ServiceException;
import com.system.roll.properites.CommonProperties;
import com.system.roll.properites.RabbitProperties;
import com.system.roll.rabbit.utils.RabbitUtil;
import com.system.roll.security.interceptor.LoginInterceptor;
import com.system.roll.utils.JsonUtil;
import com.system.roll.utils.SpringContextUtil;
import com.system.roll.webSocket.context.SocketContext;
import com.system.roll.webSocket.context.SocketContextHandler;
import com.system.roll.webSocket.handler.CustomHttpServletRequest;
import com.system.roll.webSocket.handler.SocketHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * 教师端获取实时考勤数据的长连接
 * */
@Slf4j
@Component
@ServerEndpoint(value = "/professor/roll/data/{socketId}/{token}")
public class DataSocketHandler implements SocketHandler {

    private RollDataListener listener;

    @Override
    public void open(Session session, String socketId) {}

    @OnOpen
    @Override
    public void open(Session session, @PathParam(value = "socketId")String socketId, @PathParam(value = "token")String token) throws IOException, EncodeException {
        /*进行token验证*/
        LoginInterceptor loginInterceptor = SpringContextUtil.getBean("LoginInterceptor");
        try {loginInterceptor.preHandle(new CustomHttpServletRequest().setHeader("Authorization",token).setSession(session),new Response(),token);}
        catch (ServiceException e){
            session.getBasicRemote().sendObject(Result.error(e));
            session.close();
        }
        /*将自己注册到context中*/
        SocketContextHandler.addContext("data:"+socketId,new SocketContext(this,session));
        /*创建队列*/
        RabbitUtil rabbitUtil = SpringContextUtil.getBean("RabbitUtil");
        RabbitProperties rabbitProperties = SpringContextUtil.getBean("RabbitProperties");
        CommonProperties commonProperties = SpringContextUtil.getBean("CommonProperties");
        rabbitUtil.createTTLQueue(rabbitProperties.getRollDataQueuePrefix()+socketId,rabbitProperties.getRollDataExchange(),socketId,false,commonProperties.RollDataTTL(TimeUnit.MINUTE));
        /*注册监听器，循环读取内容*/
        this.listener = new RollDataListener(socketId, rabbitProperties.getRollDataQueuePrefix() + socketId);
        this.listener.run();
    }

    @OnMessage
    @Override
    public void onMessage(String data) {

    }

    @OnClose
    @OnError
    @Override
    public void onClose(@PathParam(value = "socketId")String socketId) throws IOException {
        SocketHandler.super.onClose("data:"+socketId);
        this.listener.stop();
    }

    @Data
    public static class RollDataListener implements Runnable{
        private String socketId;
        private String queueName;
        private boolean isRunning;

        public RollDataListener(String socketId,String queueName){
            this.socketId = socketId;
            this.queueName = queueName;
            this.isRunning = true;
        }

        public void stop(){
            this.isRunning = false;
        }

        @Override
        @SuppressWarnings("all")
        public void run() {
            RabbitUtil rabbitUtil = SpringContextUtil.getBean("RabbitUtil");
            SocketContext context = SocketContextHandler.getContext("data:" + socketId);
            while (this.isRunning){
                String data = rabbitUtil.consume(queueName, 100);
                /*收到考勤数据，向前端发送*/
                try {
                    if (data!=null){
                        context.sendMessage(ResultCode.SUCCESS, JsonUtil.toObject(data, RollData.class));
                    }else {
                        Thread.sleep(900);
                    }
                }catch (Exception e){
                    try {
                        context.sendMessage(ResultCode.SERVER_ERROR,null);
                    } catch (IOException | EncodeException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
