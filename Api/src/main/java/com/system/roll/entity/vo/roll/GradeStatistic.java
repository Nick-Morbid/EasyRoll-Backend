package com.system.roll.entity.vo.roll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GradeStatistic {
    private List<GradeRollData> rollDataList;
    private Integer total;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class GradeRollData{
        private String courseName;
        private Integer classroomNo;
        private String professorName;
        private Integer lateNum;
        private List<String> lateList;
        private Integer leaveNum;
        private List<String> leaveList;
    }
}