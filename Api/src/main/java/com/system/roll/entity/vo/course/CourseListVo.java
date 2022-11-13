package com.system.roll.entity.vo.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CourseListVo {

    private List<CourseVo> courses;

    private Integer total;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class CourseVo{
        private Long id;

        private String name;

        private Integer enrollNum;

        private String  period;

        private Integer startWeek;

        private Integer endWeek;

        private String professorName;
    }
}
