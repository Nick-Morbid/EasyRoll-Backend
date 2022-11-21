package com.system.roll.entity.vo.message.supervisor.builder.impl;

import com.system.roll.entity.constant.impl.MsgType;
import com.system.roll.entity.vo.message.supervisor.builder.MessageBuilder;
import com.system.roll.entity.vo.message.Message;
import com.system.roll.entity.vo.message.supervisor.OperationNotice;

public class OperationNoticeBuilder extends MessageBuilder {
    @Override
    public Message build() {
        OperationNotice message = new OperationNotice().setMsgType(MsgType.LEAVE_APPLICATION.getCode());
        OperationNotice.MessageData data = new OperationNotice.MessageData();
        /*设置公共属性*/
        setCommon(message);
        /*设置data*/
        data.setOperationId(String.valueOf(super.get("operationId")))///*data的生成后面可以结合数据库的相关操作*/
                .setOperationLog(String.valueOf(super.get("operationLog")))
                .setIsRejected((Integer) super.get("isRejected"))
                .setRejectors((String[]) super.get("rejectors"));
        /*装配data*/
        message.setData(data);

        return message;
    }
}
