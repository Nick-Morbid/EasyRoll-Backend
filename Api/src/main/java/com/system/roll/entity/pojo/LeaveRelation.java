package com.system.roll.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString
@TableName(value = "leave_relation")
public class LeaveRelation {
    @TableId
    private String id;

    @TableField(value = "student_id")
    private String studentId;

    @TableField(value = "start_time")
    private Date startTime;

    @TableField(value = "end_time")
    private Date endTime;

    private String excuse;

    @TableField(value = "student_name")
    private String studentName;

    private String attachment;

    private Timestamp created;

    @TableField(value = "transactor_id")
    private String transactorId;

    @TableField(value = "transactor_name")
    private String transactorName;

    private Integer result;
}
