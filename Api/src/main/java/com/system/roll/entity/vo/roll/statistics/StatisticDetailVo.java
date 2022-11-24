package com.system.roll.entity.vo.roll.statistics;

import com.system.roll.entity.constant.impl.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class StatisticDetailVo {
    private String courseId;
    private String name;
    private Integer enrollNum;
    private Integer attendanceNum;
    private Integer absenceNum;
    private Integer leaveNum;
    private Integer lateNum;
    private List<Record> absenceList;
    private List<Record> leaveList;
    private List<Record> lateList;
    private Double attendanceRate;
    private Period period;
    private Integer startWeek;
    private Integer endWeek;
    private String professorName;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Record {
        private String studentId;
        private String studentName;
    }
}
