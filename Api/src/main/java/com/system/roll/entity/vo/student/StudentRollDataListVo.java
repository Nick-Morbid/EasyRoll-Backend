package com.system.roll.entity.vo.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class StudentRollDataListVo {

    private List<StudentRollData> students;
    private Integer total;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class StudentRollData{
        private String studentId;
        private String name;
        private Integer attendanceNum;
        private Integer lateNum;
        private Integer absenceNum;
        private Integer leaveNum;
    }
}
