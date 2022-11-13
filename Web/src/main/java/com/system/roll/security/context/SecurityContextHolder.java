package com.system.roll.security.context;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class SecurityContextHolder {
    private final static Map<Long,SecurityContext> contextPool = new ConcurrentHashMap<>();

    public static SecurityContext getContext(){
        return contextPool.get(Thread.currentThread().getId());
    }

    public static void setContext(SecurityContext securityContext){
        contextPool.put(Thread.currentThread().getId(),securityContext);
    }

    public static void clearContext(){
        contextPool.remove(Thread.currentThread().getId());
    }
}
