package com.system.roll.entity.constant.impl;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.system.roll.entity.constant.CommonEnum;

public enum  TeachingMode implements CommonEnum {
    EVERY_WEEK("每周都有",0),
    ODD_SINGLE_WEEK("单数周有",1),
    EVEN_SINGLE_WEEK("双数周有",2);

    private final String msg;
    @EnumValue
    private final Integer code;

    private TeachingMode(String msg,Integer code){
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