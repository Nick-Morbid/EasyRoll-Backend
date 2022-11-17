package com.system.roll.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.system.roll.constant.impl.Period;
import com.system.roll.typehandler.AutoGenericEnumTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString
@TableName(value = "state")
public class State {

    @TableId
    private String id;

    @TableField(value = "student_id")
    private String studentId;

    @TableField(value = "student_name")
    private String studentName;

    @TableField(value = "course_id")
    private String courseId;

    @TableField(typeHandler = AutoGenericEnumTypeHandler.class)
    private Period period;

    @TableField(value = "appeal_excuse")
    private String appealExcuse;

    private String attachment;

    @TableField(value = "transactor_id")
    private String transactorId;

    @TableField(value = "transactor_name")
    private String transactorName;

    private Integer result;

    private Timestamp modified;

    private Date date;
}
