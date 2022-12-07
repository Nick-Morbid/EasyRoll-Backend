package com.system.roll.entity.constant.impl;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.system.roll.entity.constant.CommonEnum;

public enum RollDataType implements CommonEnum {
    SINGLE("单条考勤数据",0),
    STATISTIC("考勤数据统计",1),
    START_ROLL("开启点名",2),
    ABORT_ROLL("中断点名",3),
    RESUME_ROLL("恢复点名",3);

    private final String msg;
    @EnumValue
    private final Integer code;

    private RollDataType(String msg,Integer code){
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
