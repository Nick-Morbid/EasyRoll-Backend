package com.system.roll.entity.vo.leave;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class LeaveVo {
    private String id;
    private String name;
    private Date startTime;
    private Date endTime;
    private String excuse;
    private Integer result;
    private String transactor;
    private String attachment;
}
