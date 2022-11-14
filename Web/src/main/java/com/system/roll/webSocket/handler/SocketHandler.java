package com.system.roll.webSocket.handler;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;

public interface SocketHandler {
    /**
     * 长连接开启
     * @param session 会话对象
     * @param socketId 路径固定参数
     * */
    void open(Session session,String socketId);
    /**
     * 长连接信息接收
     * @param data 前端数据流
     * */
    void onMessage(String data);
    /**
     * 长连接断开
     * */
    void onClose(String socketId) throws IOException;
    /**
     * 发送消息
     * */
    void sendMessage(Object data) throws IOException, EncodeException;
    /**
     * 断开连接
     * */
    void close() throws IOException;
}
