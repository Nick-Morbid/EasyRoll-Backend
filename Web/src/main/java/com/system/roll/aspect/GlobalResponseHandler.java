package com.system.roll.aspect;

import com.system.roll.entity.vo.Result;
import com.system.roll.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*
* 全局返回结果处理类
* */
@Slf4j
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
                /*
        是否开启advice支持
        true：支持，false：不支持
         */
        return true;
    }

        /*
    处理response的具体业务方法
     */

    //    可以在这里统一包装数据，并做数据脱敏
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        log.info("name of method:"+ Objects.requireNonNull(methodParameter.getMethod()).getName());
        log.info("parameter of method:"+methodParameter.getParameterName());
        log.info("return data:"+o);
        log.info("type of return data:"+(o==null?"":o.getClass()));
        //异常处理
        if ("error".equals(Objects.requireNonNull(methodParameter.getMethod()).getName())){
            log.error("error occur:"+o);
            Map course = JsonUtil.toObject(JsonUtil.toJson(o), HashMap.class);
            return Result.error(String.valueOf(course.get("error")),(int)Double.parseDouble(String.valueOf(course.get("status"))),o);
        }
        //对应返回值为void的情况
        if (o==null){
            return Result.success();
        }
        if (o instanceof String){
            return o;
        }
//        System.out.println(Objects.requireNonNull(methodParameter.getMethod()).getName());
        // 数据在返回之间已经经过了全局异常处理，并进行了相应的封装
        if (o instanceof Result){
            return o;
        }
        return Result.success(o);
    }
}
