package com.system.roll.service.student;

import com.system.roll.entity.vo.leave.LeaveListVo;
import com.system.roll.entity.vo.leave.LeaveVo;
import com.system.roll.entity.vo.student.RollHistoryVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Date;
import java.sql.Timestamp;

public interface StudentRollService {
    LeaveListVo getAllLeave();

    LeaveVo leaveQuery(String leaveId);

    void applyQuery(LeaveQueryDTO leaveDto);

    RollHistoryVo getHistory(Date date);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    @ToString
    class LeaveQueryDTO{
        private Timestamp startTime;
        private Timestamp endTime;
        private String excuse;
        private String attachment;
    }
}
