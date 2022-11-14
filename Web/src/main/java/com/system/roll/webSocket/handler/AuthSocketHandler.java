package com.system.roll.webSocket.handler;

import com.system.roll.webSocket.context.SocketContext;
import com.system.roll.webSocket.context.SocketContextHandler;
import org.springframework.stereotype.Component;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * 网页端+微信小程序授权登录的长连接（由网页端发起）
 * */
@Component
@ServerEndpoint(value = "/auth/login/web/{socketId}")
public class AuthSocketHandler implements SocketHandler{
    private Session session;

    @Override
    public void open(Session session,@PathParam(value = "socketId")String socketId) {
        this.session = session;
        /*将自己注册到context中*/
        SocketContextHandler.addContext(socketId,new SocketContext().setSocketHandler(this));
    }

    @Override
    public void onMessage(String data) {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void sendMessage(String data) throws IOException, EncodeException {
        session.getBasicRemote().sendText(data);
    }

    @Override
    public void close() throws IOException {
        session.close();
    }
}
