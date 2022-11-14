package com.system.roll.webSocket.context;

import com.system.roll.constant.impl.ResultCode;
import com.system.roll.exception.impl.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class SocketContextHandler {
    private static Map<String,SocketContext> contextPool = new ConcurrentHashMap<>();

    public static void addContext(String key,SocketContext socketContext){
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
            contextPool.get(key).getSocketHandler().close();
            contextPool.remove(key);
        }
    }
}
