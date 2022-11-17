package com.system.roll.constant.impl;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.system.roll.constant.CommonEnum;

public enum MsgType implements CommonEnum {
    OPERATION_NOTICE("督导队员操作通知消息",0),
    LEAVE_APPLICATION("学生请假申请消息",1)
    ;

    private final String msg;
    @EnumValue
    private final Integer code;

    private MsgType(String msg,Integer code){
        this.msg = msg;
        this.code = code;
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
