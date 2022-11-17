package com.system.roll.webSocket.handler.impl;

import com.system.roll.constant.impl.ResultCode;
import com.system.roll.entity.vo.roll.SingleRollStatisticVo;
import com.system.roll.properites.RabbitProperties;
import com.system.roll.rabbit.utils.RabbitUtil;
import com.system.roll.service.SupervisorRollService;
import com.system.roll.utils.SpringContextUtil;
import com.system.roll.webSocket.context.SocketContext;
import com.system.roll.webSocket.context.SocketContextHandler;
import com.system.roll.webSocket.handler.SocketHandler;
import lombok.extern.slf4j.Slf4j;
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
@ServerEndpoint(value = "/supervisor/roll/call/{socketId}")
public class RollSocketHandler implements SocketHandler {

    private String socketId;
    private Integer enrollNum;//应点名的人数
    private Integer count;//计数器
    RabbitUtil rabbitUtil;
    RabbitProperties rabbitProperties;

    /**
     * 建立督导队员发送点名考勤数据的长连接
     * @param session 会话对象
     * @param socketId 这里定义为courseId
     * */
    @OnOpen
    @Override
    public void open(Session session,@PathParam(value = "socketId") String socketId) {
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
        };
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
        SocketHandler.super.onClose(socketId);
    }
}
