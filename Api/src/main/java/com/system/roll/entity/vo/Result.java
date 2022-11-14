package com.system.roll.entity.vo;

import com.system.roll.constant.impl.ResultCode;
import com.system.roll.exception.CommonException;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 通用的返回结果封装类
 * */
@Data
@ToString
public class Result<T> implements Serializable {
    private final String message;
    private final Integer status;
    private final T data;

//    下面是success构造方法

    public static<T> Result<T> success(){
        return new Result<>(ResultCode.SUCCESS);
    }

    public static<T> Result<T> success(T data){
        return new Result<>(data);
    }

    public static<T> Result<T> success(String message){return new Result<>(message,ResultCode.SUCCESS.getCode());}

    public static<T> Result<T> success(String message,T data){
        return new Result<>(message,data);
    }

    public static<T> Result<T> success(String message,Integer status){
        return new Result<>(message,status);
    }

    public static<T> Result<T> success(String message,Integer status,T data){
        return new Result<>(message,status,data);
    }

    public static<T> Result<T> success(ResultCode state){
        return new Result<>(state);
    }

    public static<T> Result<T> success(ResultCode state,T data){
        return new Result<>(state,data);
    }

//    下面是error构造方法
    public static<T> Result<T> error(String message,Integer status){
        return new Result<>(message,status);
    }

    public static<T> Result<T> error(String message,Integer status,T data){
        return new Result<>(message,status,data);
    }

    public static<T> Result<T> error(CommonException state){
        return new Result<>(state);
    }

    public static<T> Result<T> error(CommonException state,T data){
        return new Result<>(state,data);
    }

    public static<T> Result<T> error(ResultCode state){
        return new Result<>(state);
    }

    public static<T> Result<T> error(ResultCode state,T data){
        return new Result<>(state,data);
    }




//    下面是各种构造器
    /**
     * 最基本的构造器
     * @param status 状态码
     * @param message 状态描述
     * @param data 数据部分
     * */
    private Result(String message,Integer status,T data){
        this.message = message;
        this.status = status;
        this.data = data;
    }

    /**
     * 数据部分缺省的构造器
     * @param status 状态码
     * @param message 状态描述
     * */
    private Result(String message,Integer status){
        this(message,status,null);
    }


    /**
     * 成功构造器
     * @param data 数据
     * */
    private Result(T data){
        this(ResultCode.SUCCESS.getMsg(),data);
    }

    /**
     * 成功构造器
     * @param message 描述信息
     * @param data 数据
     * */
    private Result(String message,T data){
        this(message,ResultCode.SUCCESS.getCode(),data);
    }

    /**
     * 根据状态对象进行构造
     * @param state 状态对象
     * */
    private Result(ResultCode state){
        this(state.getMsg(),state.getCode(),null);
    }

    /**
     * 根据状态对象进行构造
     * @param state 状态对象
     * @param data 数据
     * */
    private Result(ResultCode state,T data){
        this(state.getMsg(),state.getCode(),data);
    }

    /**
     * 根据异常对象进行构造
     * @param state 异常对象
     * */
    private Result(CommonException state){
        this(state.getMsg(),state.getCode(),null);
    }

    /**
     * 根据异常对象进行构造
     * */
    private Result(CommonException state,T data){
        this(state.getMsg(),state.getCode(),data);
    }

}
