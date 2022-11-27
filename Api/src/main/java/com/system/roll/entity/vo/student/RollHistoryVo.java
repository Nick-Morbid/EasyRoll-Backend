package com.system.roll.entity.vo.student;

import com.system.roll.entity.constant.impl.Period;
import com.system.roll.entity.constant.impl.RollState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RollHistoryVo {

    private List<RollRecord> rollRecords;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class RollRecord{
        private String courseId;
        private String courseName;
        private Period period;
        private RollState state;
        private String supervisorId;
        private String supervisorName;
    }
}
