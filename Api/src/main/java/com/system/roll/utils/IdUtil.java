package com.system.roll.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component(value = "IdUtil")
public class IdUtil {
    public String getId(){
        Calendar c=Calendar.getInstance();
        String time= new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(c.getTime());
        StringBuilder s=new StringBuilder(time.substring(14, 16));
        long sys=System.currentTimeMillis();
        s.append(Long.toString(sys), 11, 13);
        double tm=Math.random()*10000+1;
        s.append(Double.toString(tm).substring(Double.toString(tm).length()-4));
        return s.toString();
    }
    /**
     * 生成长连接的id
     * */
    public String getWebSocketId(){
        return "ws"+getId();
    }
}
