package com.system.roll.entity.vo.roll.statistics;

import com.system.roll.entity.constant.impl.Period;
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
public class StatisticsVo {

    private List<Record> statisticsRecords;
    private Integer total;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Record{
        private String statisticId;
        private String courseId;
        private String courseName;
        private Integer weekNo;
        private Integer weekDay;
        private Period period;
        private Date date;
        private Integer enrollNum;
        private Integer absenceNum;
        private Integer attendanceNum;
        private Integer leaveNum;
        private Integer lateNum;
        private Double attendanceRate;
    }
}
