package com.system.roll.entity.vo;

import com.system.roll.constant.impl.ResultCode;
import com.system.roll.exception.CommonException;
import lombok.Data;

/**
 * 通用的返回结果封装类
 * */
@Data
public class Result<T> {
    private final String msg;
    private final Integer code;
    private final T data;

//    下面是success构造方法

    public static<T> Result<T> success(){
        return new Result<>(ResultCode.SUCCESS);
    }

    public static<T> Result<T> success(T data){
        return new Result<>(data);
    }

    public static<T> Result<T> success(String msg){return new Result<>(msg,ResultCode.SUCCESS.getCode());}

    public static<T> Result<T> success(String msg,T data){
        return new Result<>(msg,data);
    }

    public static<T> Result<T> success(String msg,Integer code){
        return new Result<>(msg,code);
    }

    public static<T> Result<T> success(String msg,Integer code,T data){
        return new Result<>(msg,code,data);
    }

    public static<T> Result<T> success(ResultCode state){
        return new Result<>(state);
    }

    public static<T> Result<T> success(ResultCode state,T data){
        return new Result<>(state,data);
    }

//    下面是error构造方法
    public static<T> Result<T> error(String msg,Integer code){
        return new Result<>(msg,code);
    }

    public static<T> Result<T> error(String msg,Integer code,T data){
        return new Result<>(msg,code,data);
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
     * @param code 状态码
     * @param msg 状态描述
     * @param data 数据部分
     * */
    private Result(String msg,Integer code,T data){
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    /**
     * 数据部分缺省的构造器
     * @param code 状态码
     * @param msg 状态描述
     * */
    private Result(String msg,Integer code){
        this(msg,code,null);
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
     * @param msg 描述信息
     * @param data 数据
     * */
    private Result(String msg,T data){
        this(msg,ResultCode.SUCCESS.getCode(),data);
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
