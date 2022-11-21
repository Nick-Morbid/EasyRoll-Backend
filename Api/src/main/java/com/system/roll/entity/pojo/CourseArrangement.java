package com.system.roll.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.system.roll.entity.constant.impl.Period;
import com.system.roll.entity.constant.impl.TeachingMode;
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
@TableName(value = "course_arrangement")
public class CourseArrangement {
    @TableId
    private String id;

    @TableField(value = "course_id")
    private String courseId;

    @TableField(value = "classroom_no")
    private Integer classroomNo;

    @TableField(typeHandler = AutoGenericEnumTypeHandler.class)
    private Period period;

    @TableField(typeHandler = AutoGenericEnumTypeHandler.class)
    private TeachingMode mode;

    @TableField(value = "week_day")
    private Integer weekDay;
}
