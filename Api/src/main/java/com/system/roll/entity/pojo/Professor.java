package com.system.roll.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.lang.model.element.NestingKind;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Professor {

    @TableId(value = "id")
    private String id;

    @TableField(value = "professor_name")
    private String professorName;

    @TableField(value = "open_id")
    private String openId;

    @TableField(value = "department_id")
    private String departmentId;

    private Integer role;

    private Integer grade;
}
