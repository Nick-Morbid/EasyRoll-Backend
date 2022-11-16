package com.system.roll.entity.vo.message.rabbit.message.impl;

import com.system.roll.entity.vo.message.rabbit.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class LeaveApplication implements Message {
    private Timestamp time;
    private Integer msgType;
    private String content;
    private MessageData data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class MessageData{
        private String leaveId;
        private Timestamp startTime;
        private Timestamp endTime;
        private String excuse;
        private String attachment;
    }
}
