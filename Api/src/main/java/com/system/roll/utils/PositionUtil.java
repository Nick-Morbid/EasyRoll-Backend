package com.system.roll.utils;

import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.exception.impl.ServiceException;
import org.springframework.stereotype.Component;

@Component(value = "PositionUtil")
public class PositionUtil {

    /**
     * 经纬度转换为十进制坐标
     * */
    public Double toDecimal(String position){
        String[] split = position.split("\\.");
        if (split.length!=3)throw new ServiceException(ResultCode.PARAM_NOT_MATCH);
        double degrees = Double.parseDouble(split[0]);
        double minutes = Double.parseDouble(split[1]);
        double seconds = Double.parseDouble(split[2]);
        return degrees+minutes/60+seconds/3600;
    }

}
