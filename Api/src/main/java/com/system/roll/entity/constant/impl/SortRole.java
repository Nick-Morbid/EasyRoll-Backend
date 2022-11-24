package com.system.roll.entity.constant.impl;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.system.roll.entity.constant.CommonEnum;

public enum SortRole implements CommonEnum {

    STUDENT_ID("学号",0),
    ABSENT_NUM("缺课人数",1),
    ATTENDANCE_RATE("到课比例",2);


    private final String msg;
    @EnumValue
    private final Integer code;

    SortRole(String msg,Integer code){
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
