package com.system.roll.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.system.roll.entity.constant.impl.Role;
import com.system.roll.handler.typehandler.AutoGenericEnumTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString
@TableName(value = "student")
public class Student {
    @TableId
    private String id;

    @TableField(value = "student_name")
    private String studentName;

    @TableField(value = "department_id")
    private String departmentId;

    @TableField(value = "major_id")
    private String majorId;

    @TableField(value = "grade")
    private Integer grade;

    @TableField(value = "class_no")
    private Integer classNo;

    @TableField(value = "open_id")
    private String openId;

    @TableField(typeHandler = AutoGenericEnumTypeHandler.class)
    private Role role;
}
