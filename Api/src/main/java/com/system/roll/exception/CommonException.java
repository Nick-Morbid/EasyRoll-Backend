package com.system.roll.exception;

/**
 * 通用自定义异常封装类
 * */
public interface CommonException {
    /**
     * 获取错误状态描述消息
     * */
    String getMsg();
    /**
     * 获取错误状态码
     * */
    Integer getCode();

}
