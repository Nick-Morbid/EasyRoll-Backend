package com.system.roll.entity.vo.roll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RollDataVo {

    private String courseId;
    private String courseName;
    private Integer enrollNum;
    private Integer currentNum;
    private Integer attendanceNum;
    private  Integer absenceNum;
    private Integer leaveNum;
    private Date date;
    private List<Record> absenceList;
    private List<Record> leaveList;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Record{
        private String studentId;
        private String studentName;
    }
}
