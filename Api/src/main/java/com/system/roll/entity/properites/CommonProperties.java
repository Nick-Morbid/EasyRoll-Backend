package com.system.roll.entity.properites;

import com.system.roll.entity.constant.impl.Period;
import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.constant.impl.TimeUnit;
import com.system.roll.entity.exception.impl.ServiceException;
import com.system.roll.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Component(value = "CommonProperties")
@PropertySource("classpath:properties/common.properties")
public class CommonProperties {
    @Value(value = "${term.firstWeek}")
    private String firstWeek;
    @Value(value = "${term.totalWeek}")
    private Integer totalWeek;
    @Value(value = "${term.classTime}")
    private Integer classTime;
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
    @Value(value = "#{'${class.arrangement}'.split(',')}")
    private List<String> arrangement;

    private Map<ClassTime, Period> dayTimeMap;

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

    /**
     * 获取一节课的时间
     * */
    public Long ClassTime(TimeUnit timeUnit){
        long d = 0;
        switch (timeUnit){
            case SECOND:d = 1000;break;
            case MINUTE:d = 1000*60;break;
            case HOUR:d = 1000*60*60;break;
            case DAY:d = 1000*60*60*24;
        }
        return classTime*d;
    }

    public Period getPeriod(Integer hour,Integer minute){
        if (dayTimeMap==null){
            dayTimeMap = new HashMap<>();
            List<Period> periods = new ArrayList<>();
            periods.add(Period.EIGHT_TO_TEN);
            periods.add(Period.TEN_TO_TWELVE);
            periods.add(Period.FOURTEEN_TO_SIXTEEN);
            periods.add(Period.SIXTEEN_TO_EIGHTEEN);
            periods.add(Period.NINETEEN_TO_TWENTY_ONE);
            String regex = "[0-9]{2}";
            Pattern pattern = Pattern.compile(regex);
            for (int i = 0; i < arrangement.size(); i++) {
                String s = arrangement.get(i);
                Matcher matcher = pattern.matcher(s);
                Integer [] nums = new Integer[4];
                int index = 0;
                while (matcher.find()) nums[index++] = Integer.valueOf(matcher.group());
                if (index!=4) throw new ServiceException(ResultCode.PROFILE_ERROR);
                DayTime startTime = new DayTime().setHour(nums[0]).setMinute(nums[1]);
                DayTime endTime = new DayTime().setHour(nums[2]).setMinute(nums[3]);
                ClassTime classTime = new ClassTime().setStartTime(startTime).setEndTime(endTime);
                dayTimeMap.put(classTime,periods.get(i));
            }
        }
        DayTime dayTime = new DayTime().setHour(hour).setMinute(minute);
        for (ClassTime time : dayTimeMap.keySet()) if (time.getStartTime().compareTo(dayTime)<=0&&time.getEndTime().compareTo(dayTime)>=0) return dayTimeMap.get(time);
        return Period.NOT_IN_CLASS_TIME;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class DayTime implements Comparable<DayTime>{
        private Integer hour;
        private Integer minute;

        @Override
        public int compareTo(DayTime o) {
            Integer time1 = this.hour * 60 + minute;
            Integer time2 = o.getHour() * 60 + o.getMinute();
            return time1.compareTo(time2);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class ClassTime{
        private DayTime startTime;
        private DayTime endTime;
    }
}
