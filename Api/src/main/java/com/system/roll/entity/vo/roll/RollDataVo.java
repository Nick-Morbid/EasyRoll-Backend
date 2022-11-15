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
public class RollDataVo {

    private Integer enrollNum;
    private Integer finishNum;
    private Integer attendanceNum;
    private Integer leaveNum;
    private List<AbsenceList> absenceList;
    private List<LeaveList> leaveList;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class AbsenceList{
        private String studentId;
        private String studentName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class LeaveList{
        private String studentId;
        private String studentName;
    }
}
