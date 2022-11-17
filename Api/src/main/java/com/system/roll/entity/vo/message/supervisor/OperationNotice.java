package com.system.roll.entity.vo.message.supervisor;

import com.system.roll.entity.vo.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OperationNotice implements Message {
    private Timestamp time;
    private Integer msgType;
    private String content;
    private MessageData data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class MessageData{
        private String operationId;
        private String operationLog;
        private Integer isRejected;
        private String[] rejectors;
    }
}
