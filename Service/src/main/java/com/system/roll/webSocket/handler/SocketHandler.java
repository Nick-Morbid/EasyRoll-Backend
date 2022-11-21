package com.system.roll.webSocket.handler;

import com.system.roll.webSocket.context.SocketContextHandler;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import java.io.IOException;

public interface SocketHandler {
    /**
     * 长连接开启
     * @param session 会话对象
     * @param socketId 路径固定参数
     * */
    void open(Session session,@PathParam(value = "socketId")String socketId);
    /**
     * 长连接开启
     * @param session 会话对象
     * @param socketId 路径固定参数
     * @param token 身份令牌
     * */
    void open(Session session,@PathParam(value = "socketId")String socketId,@PathParam(value = "token")String token) throws IOException, EncodeException;
    /**
     * 长连接信息接收
     * @param data 前端数据流
     * */
    void onMessage(String data) throws Exception;

    /**
     * 消息加工
     * */
    default Object processMessage(Object data) {return data;}

    /**
     * 断开连接
     * */
    default void onClose(String socketId) throws IOException{
        SocketContextHandler.clearContext(socketId);//清除上下文环境
    }
}
