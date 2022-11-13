package com.system.roll.constant.impl;


import com.system.roll.constant.CommonEnum;

public enum  ResultCode implements CommonEnum {
    SUCCESS("成功",200),

    ACCESS_DENIED("权限不足",300),
    NOT_ACCESS("无访问凭证",301),
    SIGNATURE_NOT_MATCH("无效签名",302),
    TOKEN_OVERDUE("token过期",303),
    ALGORITHM_ERROR("token算法错误",304),
    TOKEN_VOID("token无效",305),


    BODY_NOT_MATCH("请求格式异常",400),
    METHOD_NOT_MATCH("请求方法异常",401),
    PATH_NOT_MATCH("请求路径异常",402),

    NULL_POINT("空指针异常",500),
    NOT_RESPONSE("服务器无响应",501),

    UNKNOWN("未知异常",600)

    ;

    private final String msg;
    private final Integer code;
    ResultCode(String msg, Integer code){
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public Integer getCode() {
        return this.code;
    }
}
