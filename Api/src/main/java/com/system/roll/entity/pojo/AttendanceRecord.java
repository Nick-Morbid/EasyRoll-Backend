package com.system.roll.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.system.roll.constant.impl.Period;
import com.system.roll.constant.impl.RollState;
import com.system.roll.typehandler.AutoGenericEnumTypeHandler;
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
    private Long id;

    @TableField(value = "student_id")
    private Long studentId;

    @TableField(value = "student_name")
    private String studentName;

    private Date date;

    @TableField(typeHandler = AutoGenericEnumTypeHandler.class)
    private Period period;

    @TableField(typeHandler = AutoGenericEnumTypeHandler.class)
    private RollState state;

    private Timestamp created;

    @TableField(value = "transactor_id")
    private Long transactorId;

    @TableField(value = "transactor_name")
    private String transactorName;
}
