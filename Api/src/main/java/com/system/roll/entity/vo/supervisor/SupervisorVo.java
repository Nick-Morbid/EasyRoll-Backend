package com.system.roll.entity.vo.supervisor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SupervisorVo {
    private String id;
    private String name;
    private String departmentId;
    private String departmentName;
    private Integer role;//1：督导队员，2：教师，3：辅导员
    private Integer currentWeek;
}
