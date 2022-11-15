package com.system.roll.entity.vo.roll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RollTaskVo {

    private Integer isPublish;
    private String courseName;
    private Timestamp startTime;
    private Timestamp endTime;
    private String promoterId;
    private String promoterName;
}
