package com.system.roll.entity.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class InfoDto{
    private String id;
    private String name;
    private String departmentName;
    private String majorName;
    private String code;
    private Integer grade;
    private Integer classNo;
}
