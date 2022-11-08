package com.system.roll.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.system.roll.constant.impl.TeachingMode;
import com.system.roll.typehandler.AutoGenericEnumTypeHandler;
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
@TableName(value = "course_arrangement")
public class CourseArrangement {
    @TableId
    private Long id;

    @TableField(value = "course_id")
    private Long courseId;

    @TableField(value = "classroom_no")
    private Integer classroomNo;

    @TableField(value = "class_time",typeHandler = AutoGenericEnumTypeHandler.class)
    private Integer classTime;

    @TableField(typeHandler = AutoGenericEnumTypeHandler.class)
    private TeachingMode mode;
}
