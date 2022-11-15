package com.system.roll.webSocket.handler.impl;

import com.system.roll.webSocket.handler.SocketHandler;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * 督导人员端发送点名数据的长连接
 * */
@Component
@ServerEndpoint(value = "/supervisor/roll/call/{socketId}")
public class RollSocketHandler implements SocketHandler {


    /**
     * 建立督导队员发送点名考勤数据的长连接
     * @param session 会话对象
     * @param socketId 这里定义为courseId
     * */
    @OnOpen
    @Override
    public void open(Session session,@PathParam(value = "socketId") String socketId) {

    }

    @OnMessage
    @Override
    public void onMessage(String data) {

    }

    @OnClose
    @OnError
    @Override
    public void onClose(@PathParam(value = "socketId") String socketId) throws IOException {
        SocketHandler.super.onClose(socketId);
    }
}
