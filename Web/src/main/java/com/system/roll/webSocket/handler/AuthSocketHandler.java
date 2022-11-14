package com.system.roll.webSocket.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.system.roll.entity.vo.Result;
import com.system.roll.entity.vo.professor.ProfessorVo;
import com.system.roll.security.jwt.JwtSecurityHandler;
import com.system.roll.utils.JsonUtil;
import com.system.roll.utils.SpringContextUtil;
import com.system.roll.webSocket.context.SocketContext;
import com.system.roll.webSocket.context.SocketContextHandler;
import org.springframework.stereotype.Component;

import javax.websocket.*;
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

    @OnOpen
    @Override
    public void open(Session session,@PathParam(value = "socketId")String socketId) {
        this.session = session;
        /*将自己注册到context中*/
        SocketContextHandler.addContext(socketId,new SocketContext().setSocketHandler(this));
    }

    @OnMessage
    @Override
    public void onMessage(String data) {

    }

    @OnClose
    @OnError
    @Override
    public void onClose(@PathParam(value = "socketId")String socketId) throws IOException {
        SocketContextHandler.clearContext(socketId);
    }

    @Override
    public void sendMessage(Object data) throws IOException {
        ProfessorVo professorVo = (ProfessorVo) ((Result<?>) data).getData();
        if (professorVo==null){
            session.getBasicRemote().sendText(JsonUtil.toJson(data));
        }else {
            JwtSecurityHandler jwtSecurityHandler = SpringContextUtil.getBean("JwtSecurityHandler");
            JsonObject jsonObject = (JsonObject) new Gson().toJsonTree(data);
            jsonObject.addProperty("Authorization",jwtSecurityHandler.getToken(professorVo.getId(),professorVo.getRole(),professorVo.getDepartmentId()));
            session.getBasicRemote().sendText(JsonUtil.toJson(jsonObject));
        }
    }

    @Override
    public void close() throws IOException {
        session.close();
    }
}
