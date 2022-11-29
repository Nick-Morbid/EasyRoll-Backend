package com.system.roll.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName(value = "course")
public class Course {
    @TableId
    private String id;

    @TableField(value = "course_name")
    private String courseName;

    @TableField(value = "enroll_num")
    private Integer enrollNum;


    @TableField(value = "start_week")
    private Integer startWeek;

    @TableField(value = "end_week")
    private Integer endWeek;

    @TableField(value = "attachment")
    private String attachment;

    @TableField(value = "classroom_no")
    private Integer classroomNo;
}
