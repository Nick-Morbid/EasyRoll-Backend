package com.system.roll.entity.exception.impl;


import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.exception.CommonException;

/**
 * 业务异常
 * */
public class ServiceException extends RuntimeException implements CommonException {
    private String msg;
    private Integer code;


    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
        this.msg = message;
    }

    public ServiceException(ResultCode state){
        this.msg = state.getMsg();
        this.code = state.getCode();
    }

    public ServiceException(Integer code,String message,Throwable cause) {
        super(message, cause);
        this.msg = message;
        this.code = code;
    }
    public ServiceException(Integer code,Throwable cause){
        this.code = code;
        this.msg = cause.getMessage();
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
