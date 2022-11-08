package com.system.roll.constant.impl;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.system.roll.constant.CommonEnum;

/**
 * 课程时间的枚举
 * */
public enum  Period implements CommonEnum {
    EIGHT_TO_TEN("早上一二节",1),
    TEN_TO_TWELVE("早上三四节",2),
    FOURTEEN_TO_SIXTEEN("下午五六节",3),
    SIXTEEN_TO_EIGHTEEN("下午七八节",4),
    NINETEEN_TO_TWENTY_ONE("晚上九十节",5);

    private final String msg;
    @EnumValue
    private final Integer code;

    private Period(String msg,Integer code){
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
