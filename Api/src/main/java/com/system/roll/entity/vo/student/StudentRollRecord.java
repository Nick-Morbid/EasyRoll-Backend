package com.system.roll.entity.vo.student;

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
public class StudentRollRecord {

    private List<Record> absenceRecords;
    private List<Record> leaveRecords;
    private List<Record> lateRecords;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Record {
        private Integer weekNo;
        private Integer weekDay;
        private Period period;
    }
}
