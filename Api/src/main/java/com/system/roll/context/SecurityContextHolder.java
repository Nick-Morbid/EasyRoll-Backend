package com.system.roll.context;

import com.system.roll.constant.impl.ResultCode;
import com.system.roll.exception.impl.ServiceException;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class SecurityContextHolder {
    private final static Map<Long,SecurityContext> contextPool = new ConcurrentHashMap<>();

    public static SecurityContext getContext(){
        if (!contextPool.containsKey(Thread.currentThread().getId())){
            contextPool.put(Thread.currentThread().getId(),new SecurityContext(new Authorization()));
        }else {
            throw new ServiceException(ResultCode.UNKNOWN);
        }
        return contextPool.get(Thread.currentThread().getId());
    }

    public static void setContext(SecurityContext securityContext){
        contextPool.put(Thread.currentThread().getId(),securityContext);
    }

    public static void clearContext(){
        contextPool.remove(Thread.currentThread().getId());
    }
}
