package com.system.roll.constant.impl;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.system.roll.constant.CommonEnum;

public enum ClassMode implements CommonEnum {
        ;

    private final String msg;
    @EnumValue
    private final Integer code;

    private ClassMode(String msg,Integer code){
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
