package com.system.roll.entity.vo.message.supervisor.builder;

import com.system.roll.entity.constant.impl.MsgType;
import com.system.roll.entity.constant.impl.ResultCode;
import com.system.roll.entity.vo.message.Message;
import com.system.roll.entity.vo.message.supervisor.builder.impl.LeaveApplicationBuilder;
import com.system.roll.entity.vo.message.supervisor.builder.impl.OperationNoticeBuilder;
import com.system.roll.entity.exception.impl.ServiceException;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * 构建消息的方法
 * */
@Data
@Accessors(chain = true)
public abstract class MessageBuilder {

    /*用来构造消息的参数*/
    protected Map<String,Object> fieldMap = new HashMap<>();

    /*公共参数*/
    protected Timestamp time;
    protected String content;

    /*从fieldMap中获取参数的值*/
    protected  Object get(String fieldName){
        if (!fieldMap.containsKey(fieldName)) throw new ServiceException(ResultCode.FAILED_TO_GET_ELEMENT_FROM_MAP);
        return fieldMap.get(fieldName);
    }

    /*给fieldMap添加值（用于data的构建）*/
    public MessageBuilder setData(String fieldName,Object fieldValue){
        fieldMap.put(fieldName,fieldValue);
        return this;
    }

    /**
     * 设置公共属性
     * */
    protected void setCommon(Message message){
        message.setContent(this.content);
        message.setTime(this.time);
    }

    /*获取建造者实例*/
    public static MessageBuilder getBuilder(MsgType msgType){
        MessageBuilder builder = null;
        switch (msgType){
            case OPERATION_NOTICE:builder = new OperationNoticeBuilder();break;
            case LEAVE_APPLICATION:builder = new LeaveApplicationBuilder();break;
        }
        return builder;
    }

    public abstract Message build();
}
