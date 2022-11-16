package com.system.roll.entity.pojo;
import com.system.roll.constant.impl.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * 业务操作日志
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OperationLog {
    private String id;
    private Timestamp time;
    private String outline;
    private String operationLog;
    private OperationType operationType;
    private String attachmentId;
}
