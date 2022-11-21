package com.system.roll.webSocket.context;

import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.vo.Result;
import com.system.roll.webSocket.handler.SocketHandler;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;

/**
 * SocketContext的作用：对外隔离socketHandler的操作，可以对socketHandler的方法进行增强（某种装饰模式）
 * */
@Data
@Slf4j
@Accessors(chain = true)
public class SocketContext {
    private SocketHandler socketHandler;
    private Session session;

    public SocketContext(SocketHandler socketHandler,Session session){
        this.socketHandler = socketHandler;
        this.session = session;
        log.info("[Thread:{}]a new ws session,id is:{}",Thread.currentThread().getId(),session.getId());
    }

    /*发送消息（所有类型的socketHandler的消息发送操作都是一样，所以写在Context类中）*/
    public void sendMessage(ResultCode state, Object data) throws IOException, EncodeException {
        /*对消息进行加工*/
        Object processedData = socketHandler.processMessage(data);
        /*组装消息*/
        Result<?> result = Result.success(state, processedData);
        /*发送消息*/
        session.getBasicRemote().sendObject(result);
    }

    /*销毁操作*/
    public void clear() throws IOException {
        this.session.close();
        log.info("[Thread:{}]a ws session closed,id is:{}",Thread.currentThread().getId(),session.getId());
    }
}
