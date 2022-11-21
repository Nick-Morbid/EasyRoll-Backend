package com.system.roll.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.system.roll.entity.constant.impl.Period;
import com.system.roll.entity.constant.impl.RollState;
import com.system.roll.handler.typehandler.AutoGenericEnumTypeHandler;
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
@TableName(value = "attendance_record")
public class AttendanceRecord {
    @TableId
    private String id;

    @TableField(value = "student_id")
    private String studentId;

    @TableField(value = "student_name")
    private String studentName;

    private Date date;

    @TableField(typeHandler = AutoGenericEnumTypeHandler.class)
    private Period period;

    @TableField(typeHandler = AutoGenericEnumTypeHandler.class)
    private RollState state;

    private Timestamp created;

    @TableField(value = "transactor_id")
    private String supervisorId;

    @TableField("course_id")
    private String courseId;
}
