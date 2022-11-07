package com.system.roll.constant.impl;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.system.roll.constant.CommonEnum;

public enum  Role implements CommonEnum {
    ADMIN("管理员",0),
    NORMAL("普通用户",1)
    ;

    private final String msg;
    @EnumValue
    private final Integer code;

    private Role(String msg,Integer code){
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
