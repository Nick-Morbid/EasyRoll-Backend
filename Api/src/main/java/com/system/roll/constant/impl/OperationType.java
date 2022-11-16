package com.system.roll.constant.impl;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.system.roll.constant.CommonEnum;

public enum OperationType implements CommonEnum {
    UPLOAD_COURSE("上传了课程信息",0),
    UPDATE_COURSE("更新了课程信息",1),
    DELETE_COURSE("删除了课程信息",2),
    SOLVE_LEAVE_APPLICATION("处理请假申请",3),
    TAKE_A_ROLL("进行了考勤工作",4),
    NO_OPERATION("无操作",10)
    ;

    private final String msg;
    @EnumValue
    private final Integer code;

    private OperationType(String msg,Integer code){
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
