package com.system.roll.context.security;

import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.exception.impl.ServiceException;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class SecurityContextHolder {
    private final static Map<Long,SecurityContext> contextPool = new ConcurrentHashMap<>();

    public static SecurityContext getContext(){
        if (!contextPool.containsKey(Thread.currentThread().getId())){
            throw new ServiceException(ResultCode.FAILED_TO_GET_ELEMENT_FROM_MAP);
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
