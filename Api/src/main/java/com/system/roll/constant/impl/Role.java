package com.system.roll.constant.impl;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.system.roll.constant.CommonEnum;

public enum  Role implements CommonEnum {
    STUDENT("学生",0),
    SUPERVISOR("督导队员",1),
    PROFESSOR("教授",2),
    INSTRUCTOR("辅导员",3)
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
