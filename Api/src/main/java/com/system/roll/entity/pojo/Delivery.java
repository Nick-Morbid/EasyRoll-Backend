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
@TableName(value = "delivery")
public class Delivery {
    @TableId
    private String id;

    @TableField(value = "professor_id")
    private String professorId;

    @TableField(value = "professor_name")
    private String professorName;

    @TableField(value = "course_id")
    private String courseId;
}
