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
@TableName(value = "roll_relation")
public class RollRelation {

    @TableId
    private String id;

    @TableField(value = "supervisor_id")
    private String supervisor_id;

    @TableField(value = "course_id")
    private String courseId;
}
