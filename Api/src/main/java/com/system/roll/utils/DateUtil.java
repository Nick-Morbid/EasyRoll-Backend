package com.system.roll.utils;

import com.system.roll.constant.impl.ResultCode;
import com.system.roll.exception.impl.ServiceException;
import com.system.roll.properites.CommonProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 日期工具
 * */
@Slf4j
@Component
public class DateUtil {

    @Resource
    private CommonProperties commonProperties;

    public static Integer SECOND = 1000;//1s = 1000ms
    public static Integer MINUTE = 60*SECOND;//1m = 60s
    public static Integer HOUR = 60*MINUTE;//1h = 60m
    public static Integer DAY = 24*HOUR;//1d = 24h


    /**
     * 获取本学期第一周的星期一的date对象
     * */
    public Date getFirstWeek(){
        return stringToDate(commonProperties.getFirstWeek());
    }

    /**
     * 获取当前日期对应的周数
     * */
    public Integer getWeek(Date date){
        Date firstWeek = getFirstWeek();
        int deltaDay = Math.toIntExact((date.getTime() - firstWeek.getTime()) / DAY);
        return (deltaDay+7)/7;
    }

    /**
     * 根据字符串生成对应的date对象
     * @param dateString 要进行转换的字符串
     * */
    public Date stringToDate(String dateString){
        /*创建时间格式化模板*/
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return new Date(format.parse(dateString).getTime());
        } catch (ParseException e) {
            log.error("[Thread:{}]时间格式转换失败",Thread.currentThread().getId());
            throw new ServiceException(ResultCode.SERVER_ERROR);
        }
    }
}
