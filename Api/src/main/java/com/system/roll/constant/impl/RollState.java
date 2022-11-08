package com.system.roll.constant.impl;

import com.system.roll.constant.CommonEnum;

/**
 * 考勤状态枚举
 * */
public enum  RollState implements CommonEnum {
    LEAVE("请假",1),
    ABSENCE("缺勤",2);

    private final String msg;
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
