package com.system.roll.entity.vo.roll;

import com.system.roll.entity.constant.impl.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 对应教师端【数据导出】界面
 * 课程的考勤数据统计
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TotalRollStatisticVo {
    private Integer enrollNum;
    private List<Record> statisticsRecords;
    private Integer total;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Record{
        private String statisticId;
        private Integer weekNo;
        private Integer weekDay;
        private Period period;
        private Integer attendanceNum;
        private Integer lateNum;
        private Integer absenceNum;
        private Integer leaveNum;
        private Double attendanceRate;
    }
}
