package com.system.roll.entity.constant.impl;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.system.roll.entity.constant.CommonEnum;

public enum SortRole implements CommonEnum {

    DELIVERY_TIME("上课时间",0),
    STUDENT_ID("学号",1),
    ABSENT_NUM("缺课人数",2),
    ATTENDANCE_RATE("到课比例",3);


    private final String msg;
    @EnumValue
    private final Integer code;

    private SortRole(String msg,Integer code){
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
