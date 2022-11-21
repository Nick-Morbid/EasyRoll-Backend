package com.system.roll.entity.vo.supervisor;

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
    private String majorId;
    private String major;
    private Integer grade;
    private Integer classNo;
    private Integer currentWeek;
}
