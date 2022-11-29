package com.system.roll.entity.constant.impl;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.system.roll.entity.constant.CommonEnum;

public enum BuildingNo implements CommonEnum {
    WEST_THREE("西3",1),
    WEST_TWO("西2",2),
    WEST_ONE("西1",3),
    MEDIUM("中楼",4),
    EAST_ONE("东1",5),
    EAST_TWO("东2",6),
    EAST_THREE("东3",7),
    ART_ONE("文1",8),
    ART_TWO("文2",9),
    ART_THREE("文3",10),
    ART_FOUR("文4",11)
    ;

    private final String msg;
    @EnumValue
    private final Integer code;

    private BuildingNo(String msg,Integer code){
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
