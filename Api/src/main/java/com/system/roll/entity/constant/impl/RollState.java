package com.system.roll.entity.constant.impl;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.system.roll.entity.constant.CommonEnum;

/**
 * 考勤状态枚举
 * */
public enum  RollState implements CommonEnum {
    ATTENDANCE("出勤",0),
    LEAVE("请假",1),
    LATE("迟到",2),
    ABSENCE("缺勤",3);

    private final String msg;
    @EnumValue
    private final Integer code;

    private RollState(String msg,Integer code){
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
