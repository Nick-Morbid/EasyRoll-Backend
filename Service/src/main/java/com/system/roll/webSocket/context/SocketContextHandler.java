package com.system.roll.webSocket.context;

import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.exception.impl.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Slf4j
@AllArgsConstructor
@Accessors(chain = true)
public class SocketContextHandler {
    private static Map<String,SocketContext> contextPool = new ConcurrentHashMap<>();

    public static void addContext(String key,SocketContext socketContext){
        log.warn("第三方授权长连接的socketId为：{}",key);
        contextPool.put(key,socketContext);
    }

    public static SocketContext getContext(String key){
        if (!contextPool.containsKey(key)){
            throw new ServiceException(ResultCode.WEBSOCKET_NOT_BUILT);
        }
        return contextPool.get(key);
    }

    public static void clearContext(String key) throws IOException {
        if (contextPool.containsKey(key)){
            contextPool.get(key).clear();//清除上下文
            contextPool.remove(key);//移除记录
        }
    }
}
