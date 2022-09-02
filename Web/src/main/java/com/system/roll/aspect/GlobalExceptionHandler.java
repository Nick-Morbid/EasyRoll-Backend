package com.system.roll.aspect;

import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.vo.Result;
import com.system.roll.entity.exception.impl.AuthException;
import com.system.roll.entity.exception.impl.ServiceException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
* 全局异常处理类
* */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthException.class)
    public Result<?> AuthExceptionHandler(AuthException e){
//        System.out.println("权限异常！原因是："+e.getMsg());
        return Result.error(e);
    }

    @ExceptionHandler(ServiceException.class)
    public Result<?> ServiceExceptionHandler(ServiceException e){
//        System.out.println("业务异常！原因是："+e.getMsg());
        return Result.error(e);
    }

    /*
    处理空指针异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    public Result<?> NullPointerExceptionHandler(Exception e){
//        System.out.println("空指针异常！原因是："+e);
        e.printStackTrace();
        return Result.error(ResultCode.NULL_POINT);
    }

    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public Result<?> HttpMediaTypeNotSupportedExceptionHandler(){
//        System.out.println("请求格式异常！原因是："+e);
        return Result.error(ResultCode.BODY_NOT_MATCH);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public Result<?> HttpRequestMethodNotSupportedExceptionHandler(){
//        System.out.println("请求方法异常！原因是："+e);
        return Result.error(ResultCode.METHOD_NOT_MATCH);
    }

    @ExceptionHandler(value = ClassCastException.class)
    public Result<?> ClassCastExceptionHandler(ClassCastException e){
        System.out.println("请求路径异常！原因是："+e);
        return Result.error(ResultCode.PATH_NOT_MATCH);
    }

    /*
    处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    public Result<?> ExceptionHandler(Exception e){
        System.out.println("未知异常！原因是："+e);
        return Result.error(ResultCode.UNKNOWN);
    }
}
