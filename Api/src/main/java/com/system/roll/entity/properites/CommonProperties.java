package com.system.roll.entity.properites;

import com.system.roll.entity.constant.impl.TimeUnit;
import com.system.roll.utils.DateUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Date;

@Data
@Component(value = "CommonProperties")
@PropertySource("classpath:properties/common.properties")
public class CommonProperties {
    @Value(value = "${term.firstWeek}")
    private String firstWeek;
    @Value(value = "${term.totalWeek}")
    private Integer totalWeek;
    @Value(value = "${rollData.ttl}")
    private Integer rollDataTTL;
    @Value(value = "${rollData.expire}")
    private Integer rollDataExpire;
    @Value(value = "${message.ttl}")
    private Integer messageTTL;
    @Value(value = "${message.expire}")
    private Integer messageExpire;
    @Value(value = "${snapshot.expire}")
    private Integer snapshotExpire;

    @Resource
    private DateUtil dateUtil;

    /**
     * 获取本学期第一周的星期一的date对象
     * */
    public Date FirstWeek(){
        return dateUtil.stringToDate(this.firstWeek);
    }

    /**
     * 获取考勤数据在rabbitMq中的过期时间
     * */
    public Long RollDataTTL(TimeUnit timeUnit){
        long d = 0;
        switch (timeUnit){
            case SECOND:d = 1000;break;
            case MINUTE:d = 1000*60;break;
            case HOUR:d = 1000*60*60;break;
            case DAY:d = 1000*60*60*24;
        }
        return rollDataTTL*d;
    }

    /**
     * 获取考勤数据在redis中的过期时间
     * */
    public Long RollDataExpire(TimeUnit timeUnit){
        long d = 0;
        switch (timeUnit){
            case SECOND:d = 1;break;
            case MINUTE:d = 60;break;
            case HOUR:d = 60*60;break;
            case DAY:d = 60*60*24;
        }
        return rollDataExpire*d;
    }

    /**
     * 获取消息在rabbitMq中的过期时间
     * */
    public Long MessageTTL(TimeUnit timeUnit){
        long d = 0;
        switch (timeUnit){
            case SECOND:d = 1000;break;
            case MINUTE:d = 1000*60;break;
            case HOUR:d = 1000*60*60;break;
            case DAY:d = 1000*60*60*24;
        }
        return messageTTL*d;
    }

    /**
     * 获取消息在redis中的过期时间
     * */
    public Long MessageExpire(TimeUnit timeUnit){
        long d = 0;
        switch (timeUnit){
            case SECOND:d = 1;break;
            case MINUTE:d = 60;break;
            case HOUR:d = 60*60;break;
            case DAY:d = 60*60*24;
        }
        return messageExpire*d;
    }

    /**
     * 获取快照在redis中的过期时间
     * */
    public Long ShapshotExpire(TimeUnit timeUnit){
        long d = 0;
        switch (timeUnit){
            case SECOND:d = 1;break;
            case MINUTE:d = 60;break;
            case HOUR:d = 60*60;break;
            case DAY:d = 60*60*24;
        }
        return snapshotExpire*d;
    }


}
