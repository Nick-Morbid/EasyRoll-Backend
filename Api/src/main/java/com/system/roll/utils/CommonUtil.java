package com.system.roll.utils;

import com.system.roll.entity.constant.impl.BuildingNo;
import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.exception.impl.ServiceException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 通用工具
 * */
@Component
public class CommonUtil {

    @Resource
    private EnumUtil enumUtil;

    public String getClassroom(Integer classroomNo){
        if (classroomNo<1000||classroomNo>12000) throw new ServiceException(ResultCode.PARAM_NOT_MATCH);
        int num = classroomNo / 1000;
        BuildingNo buildingNo = enumUtil.getEnumByCode(BuildingNo.class, num);
        return buildingNo.getMsg()+"-"+(classroomNo%1000);
    }
}
