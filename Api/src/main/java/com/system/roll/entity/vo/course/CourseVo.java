package com.system.roll.entity.vo.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CourseVo {

    private String id;

    private String name;

    private Integer enrollNum;

    private String  period;

    private Integer startWeek;

    private Integer endWeek;

    private String professorName;
}
