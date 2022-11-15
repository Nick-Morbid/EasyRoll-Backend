package com.system.roll.entity.vo.roll;

import com.system.roll.constant.impl.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RollStatistic {
    private Integer enrollNum;
    private List<Record> rollRecords;
    private Integer total;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Record{
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
