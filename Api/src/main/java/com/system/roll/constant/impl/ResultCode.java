package com.system.roll.constant.impl;


import com.system.roll.constant.CommonEnum;

public enum  ResultCode implements CommonEnum {
    SUCCESS("成功",200),
    NOT_REGISTER("尚未注册",201),

    ACCESS_DENIED("权限不足",300),
    NOT_ACCESS("无访问凭证",301),
    SIGNATURE_NOT_MATCH("无效签名",302),
    TOKEN_OVERDUE("token过期",303),
    ALGORITHM_ERROR("token算法错误",304),
    TOKEN_VOID("token无效",305),
    CODE_VOID("code无效",306),
    ACCESS_TOKEN_VOID("access_token无效",307),


    BODY_NOT_MATCH("请求格式异常",400),
    METHOD_NOT_MATCH("请求方法异常",401),
    PATH_NOT_MATCH("请求路径异常",402),


    NULL_POINT("空指针异常",500),
    SERVER_ERROR("服务器异常",501),
    API_CALLED_FAILED("接口调用失败",502),
    FAILED_TO_RESPONSE_RESOURCE("资源返回失败",503),

    WEBSOCKET_NOT_BUILT("长连接未建立",601),
    WEBSOCKET_SEND_FAILED("长连接消息发送失败",602),

    UNKNOWN("未知异常",700)

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
