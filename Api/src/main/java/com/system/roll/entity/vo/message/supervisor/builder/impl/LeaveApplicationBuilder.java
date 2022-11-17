package com.system.roll.entity.vo.message.supervisor.builder.impl;

import com.system.roll.constant.impl.MsgType;
import com.system.roll.entity.vo.message.Message;
import com.system.roll.entity.vo.message.supervisor.builder.MessageBuilder;
import com.system.roll.entity.vo.message.supervisor.LeaveApplication;

import java.sql.Timestamp;

public class LeaveApplicationBuilder extends MessageBuilder {
    @Override
    public Message build() {
        LeaveApplication message = new LeaveApplication().setMsgType(MsgType.LEAVE_APPLICATION.getCode());
        LeaveApplication.MessageData data = new LeaveApplication.MessageData();
        /*设置公共属性*/
        this.setCommon(message);
        /*配置对应的data*/
        data.setLeaveId(String.valueOf(super.get("leaveId")))//data的生成后面可以结合数据库的相关操作
                .setStartTime((Timestamp) super.get("startTime"))
                .setEndTime((Timestamp) super.get("endTime"))
                .setExcuse(String.valueOf(super.get("excuse")))
                .setAttachment(String.valueOf(super.get("attachment")));
        /*装配data*/
        message.setData(data);

        return message;
    }
}
