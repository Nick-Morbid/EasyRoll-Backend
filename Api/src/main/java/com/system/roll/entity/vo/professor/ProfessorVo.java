package com.system.roll.entity.vo.professor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ProfessorVo {
    private String id;
    private String name;
    private String departmentId;
    private String departmentName;
    private Integer role;
    private Integer currentWeek;
    private List<CourseVo> courses;
    private Integer weekDay;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class CourseVo{
        private String id;
        private String name;
    }
}
