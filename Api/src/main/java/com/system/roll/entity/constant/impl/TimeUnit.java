package com.system.roll.entity.constant.impl;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.system.roll.entity.constant.CommonEnum;

public enum TimeUnit implements CommonEnum {
    SECOND("秒",0),
    MINUTE("分",1),
    HOUR("小时",2),
    DAY("天",3)
    ;

    private final String msg;
    @EnumValue
    private final Integer code;

    private TimeUnit(String msg,Integer code){
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
