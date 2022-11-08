package com.system.roll.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString
@TableName(value = "leave")
public class Leave {
    @TableId
    private Long id;

    @TableField(value = "student_id")
    private Long studentId;

    @TableField(value = "start_time")
    private Timestamp startTime;

    @TableField(value = "end_time")
    private Timestamp endTime;

    private String excuse;

    @TableField(value = "student_name")
    private String studentName;

    private String attachment;
}
