package com.system.roll.entity.vo.professor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class InfoVo{
    private String id;
    private String name;
    private String departmentId;
    private String departmentName;
    private Integer role;
    private Integer currentWeek;
}
